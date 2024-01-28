package com.mathsena.stockorderapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathsena.stockorderapi.dto.OrderItemDTO;
import com.mathsena.stockorderapi.dto.StockMovementDTO;
import com.mathsena.stockorderapi.mappers.StockMovementMapper;
import com.mathsena.stockorderapi.model.*;
import com.mathsena.stockorderapi.repository.ItemRepository;
import com.mathsena.stockorderapi.repository.OrderItemRepository;
import com.mathsena.stockorderapi.repository.OrderRepository;
import com.mathsena.stockorderapi.repository.StockMovementRepository;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockMovementService {
  private final StockMovementRepository stockMovementRepository;
  private final StockMovementMapper stockMovementMapper;
  private final ItemRepository itemRepository;
  private final LoggingService loggingService;

  private final JavaMailSender javaMailSender;

  private final OrderRepository orderRepository;

  private final KafkaProducerService kafkaProducerService;
  private final ObjectMapper objectMapper;

  public StockMovementService(
      StockMovementRepository stockMovementRepository,
      StockMovementMapper stockMovementMapper,
      ItemRepository itemRepository,
      LoggingService loggingService,
      JavaMailSender javaMailSender,
      OrderRepository orderRepository,
      KafkaProducerService kafkaProducerService,
      ObjectMapper objectMapper) {
    this.stockMovementRepository = stockMovementRepository;
    this.stockMovementMapper = stockMovementMapper;
    this.itemRepository = itemRepository;
    this.loggingService = loggingService;
    this.javaMailSender = javaMailSender;
    this.orderRepository = orderRepository;
    this.kafkaProducerService = kafkaProducerService;
    this.objectMapper = objectMapper;
  }

  public List<StockMovement> listAllStockMovements() {
    return stockMovementRepository.findAll();
  }

  public StockMovement createStockMovement(StockMovementDTO stockMovementDTO) {
    StockMovement stockMovement = stockMovementMapper.toEntity(stockMovementDTO);

    Long itemId = stockMovementDTO.getItemId();
    Item item =
        itemRepository
            .findById(itemId)
            .orElseThrow(() -> new EntityNotFoundException("Item not found"));

    stockMovement.setItem(item);
    stockMovementRepository.save(stockMovement);

    allocateStockToPendingOrders(itemId, stockMovement.getQuantity());
    loggingService.logStockMovement(stockMovement);

    try {
      StockMovementDTO savedStockMovementDTO = stockMovementMapper.toDto(stockMovement);
      String stockMovementJson = objectMapper.writeValueAsString(savedStockMovementDTO);
      kafkaProducerService.sendMessage("stock-movement-created-topic", stockMovementJson);
      log.info("Stock movement created event published to Kafka");
    } catch (JsonProcessingException e) {
      log.error("Error while serializing stock movement data to JSON", e);
    }

    return stockMovement;
  }

  private void allocateStockToPendingOrders(Long itemId, Integer quantity) {
    List<Order> pendingOrders = orderRepository.findPendingOrdersByItemId(itemId);

    for (Order order : pendingOrders) {
      for (OrderItem orderItem : order.getOrderItems()) {
        if (orderItem.getItem().getId().equals(itemId)) {
          int neededQuantity = orderItem.getQuantity() - orderItem.getAllocatedQuantity();
          int quantityToAllocate = Math.min(neededQuantity, quantity);

          orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() + quantityToAllocate);
          quantity -= quantityToAllocate;

          if (quantity <= 0) break;
        }
      }

      checkAndCompleteOrder(order);

      if (quantity <= 0) break;
    }
  }

  private void checkAndCompleteOrder(Order order) {
    boolean isComplete =
        order.getOrderItems().stream()
            .allMatch(item -> item.getQuantity().equals(item.getAllocatedQuantity()));

    if (isComplete) {

      List<OrderItemDTO> orderItems =
          order.getOrderItems().stream()
              .map(this::convertOrderItemToDTO)
              .collect(Collectors.toList());

      order.setCompleted(true);

      sendEmailNotification(order.getUser(), orderItems);
    }

    orderRepository.save(order);
  }

  private OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
    OrderItemDTO orderItemDTO = new OrderItemDTO();
    orderItemDTO.setItemId(orderItem.getItem().getId());
    orderItemDTO.setQuantity(orderItem.getQuantity());
    return orderItemDTO;
  }

  public StockMovement updateStockMovement(StockMovement stockMovement) {
    if (stockMovement.getId() != null
        && stockMovementRepository.existsById(stockMovement.getId())) {
      return stockMovementRepository.save(stockMovement);
    }
    return null;
  }

  public void deleteStockMovement(Long id) {
    if (stockMovementRepository.existsById(id)) {
      stockMovementRepository.deleteById(id);
    }
  }

  public List<StockMovement> listStockMovementsByProductId(Long itemId) {
    return stockMovementRepository.findByItemId(itemId);
  }

  public void sendEmailNotification(User user, List<OrderItemDTO> orderItems) {
    if (user.getEmail() == null) {
      log.error(
          "Cannot send email notification, email address is null for user {}", user.getName());
      return;
    }

    try {
      String subject = "Order has completed successfully";
      StringBuilder content = new StringBuilder("Your order has been successfully completed.\n\n");

      // Add item details to the content
      for (OrderItemDTO orderItem : orderItems) {
        content.append("Item ID: ").append(orderItem.getItemId()).append("\n");
        content.append("Item Name: ").append(getItemNameById(orderItem.getItemId())).append("\n");
        content.append("Quantity: ").append(orderItem.getQuantity()).append("\n\n");
      }

      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);

      helper.setTo(user.getEmail());
      helper.setSubject("Dear " + user.getName() + ", " + subject + "!");
      helper.setText(content.toString(), false);

      javaMailSender.send(message);

      loggingService.logEmailSent(user, "Your order has been completed successfully.");
    } catch (Exception e) {
      e.printStackTrace();

      loggingService.logError("Intentional error, configure your GMAIL with SMTP");
      loggingService.logError(e.getMessage());
    }
  }

  private String getItemNameById(Long itemId) {
    return itemRepository
        .findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId))
        .getName();
  }
}
