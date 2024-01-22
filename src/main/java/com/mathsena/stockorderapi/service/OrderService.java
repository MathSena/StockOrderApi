package com.mathsena.stockorderapi.service;

import com.mathsena.stockorderapi.dto.OrderDTO;
import com.mathsena.stockorderapi.dto.OrderItemDTO;
import com.mathsena.stockorderapi.dto.StockMovementDTO;
import com.mathsena.stockorderapi.mappers.OrderItemMapper;
import com.mathsena.stockorderapi.mappers.OrderMapper;
import com.mathsena.stockorderapi.model.Order;
import com.mathsena.stockorderapi.model.OrderItem;
import com.mathsena.stockorderapi.model.User;
import com.mathsena.stockorderapi.repository.*;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;
  private final OrderItemRepository orderItemRepository;
  private final StockMovementRepository stockMovementRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final JavaMailSender javaMailSender;

  private final LoggingService loggingService;
  private final StockMovementService stockMovementService;

  public OrderService(
      OrderRepository orderRepository,
      OrderMapper orderMapper,
      OrderItemMapper orderItemMapper,
      OrderItemRepository orderItemRepository,
      StockMovementRepository stockMovementRepository,
      UserRepository userRepository,
      ItemRepository itemRepository,
      JavaMailSender javaMailSender,
      LoggingService loggingService,
      StockMovementService stockMovementService) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
    this.orderItemMapper = orderItemMapper;
    this.orderItemRepository = orderItemRepository;
    this.stockMovementRepository = stockMovementRepository;
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
    this.javaMailSender = javaMailSender;
    this.loggingService = loggingService;
    this.stockMovementService = stockMovementService;
  }

  public List<OrderDTO> getAllOrders() {
    return orderRepository.findAll().stream().map(orderMapper::toDto).collect(Collectors.toList());
  }

  public OrderDTO getOrderById(Long id) {
    return orderRepository.findById(id).map(orderMapper::toDto).orElse(null);
  }

  @Transactional
  public OrderDTO createOrder(OrderDTO newOrderDTO) {

    Order order = orderMapper.toEntity(newOrderDTO);

    if (newOrderDTO.getUserId() != null) {
      User user =
          userRepository
              .findById(newOrderDTO.getUserId())
              .orElseThrow(
                  () ->
                      new EntityNotFoundException(
                          "User not found with id: " + newOrderDTO.getUserId()));
      order.setUser(user);
    }

    List<OrderItem> orderItems = new ArrayList<>();
    boolean isComplete = true;

    for (OrderItemDTO itemDTO : newOrderDTO.getOrderItems()) {
      OrderItem orderItem = orderItemMapper.toEntity(itemDTO);
      orderItem.setOrder(order);
      Integer stockAvailable = checkStockForItem(itemDTO.getItemId());

      if (stockAvailable >= itemDTO.getQuantity()) {
        log.info("Item {} has enough stock to fulfill the order", itemDTO.getItemId());
        orderItem.setAllocatedQuantity(itemDTO.getQuantity());
        createStockMovement(itemDTO.getItemId(), -itemDTO.getQuantity());
      } else {
        orderItem.setAllocatedQuantity(stockAvailable);
        if (stockAvailable > 0) {
          createStockMovement(itemDTO.getItemId(), -stockAvailable);
        }
        log.info("Item {} does not have enough stock to fulfill the order", itemDTO.getItemId());
        log.info("The item will be aloocated with {} units", stockAvailable);
        isComplete =
            false; // O pedido não pode ser completo se qualquer item não tiver estoque suficiente
      }

      orderItems.add(orderItem);
    }

    order.setOrderItems(orderItems);
    order.setCompleted(isComplete);

    Order savedOrder = orderRepository.save(order);

    if (savedOrder.isCompleted()) {
      log.info("Order {} has been completed successfully", savedOrder.getId());
      log.info("Sending email notification to user {}", savedOrder.getUser().getName());
      loggingService.logOrderCompletion(savedOrder);
      sendEmailNotification(savedOrder.getUser(), newOrderDTO.getOrderItems());
    } else {
      log.info("Order {} could not be completed due to insufficient stock", savedOrder.getId());
      loggingService.noOrderCompletion(savedOrder);
    }
    return orderMapper.toDto(savedOrder);
  }

  private void createStockMovement(Long itemId, int quantity) {
    StockMovementDTO stockMovementDTO = new StockMovementDTO();
    stockMovementDTO.setItemId(itemId);
    stockMovementDTO.setQuantity(quantity);
    stockMovementService.createStockMovement(stockMovementDTO);
  }

  private Integer checkStockForItem(Long itemId) {
    Integer totalStock = stockMovementRepository.findAvailableByItemId(itemId);
    Integer totalAllocated = orderItemRepository.findTotalOrderedQuantityByItemId(itemId);

    totalAllocated = totalAllocated != null ? totalAllocated : 0;

    return totalStock - totalAllocated;
  }

  public OrderDTO updateOrder(Long id, OrderDTO updatedOrderDTO) {
    Optional<Order> optionalOrder = orderRepository.findById(id);

    if (optionalOrder.isPresent()) {
      Order existingOrder = optionalOrder.get();

      if (updatedOrderDTO.getCreationDate() != null) {
        existingOrder.setCreationDate(updatedOrderDTO.getCreationDate());
      }

      if (updatedOrderDTO.getOrderItems() != null) {
        for (OrderItemDTO updatedOrderItemDTO : updatedOrderDTO.getOrderItems()) {
          Long updatedItemId = updatedOrderItemDTO.getItemId();
          Integer updatedQuantity = updatedOrderItemDTO.getQuantity();

          Optional<OrderItem> optionalExistingOrderItem =
              existingOrder.getOrderItems().stream()
                  .filter(orderItem -> orderItem.getItem().getId().equals(updatedItemId))
                  .findFirst();

          if (optionalExistingOrderItem.isPresent()) {
            OrderItem existingOrderItem = optionalExistingOrderItem.get();
            Integer availableStock = checkStockForItem(updatedItemId);

            if (availableStock >= updatedQuantity) {
              existingOrderItem.setQuantity(updatedQuantity);
            } else {
              throw new IllegalArgumentException(
                  "Insufficient stock for item with id: " + updatedItemId);
            }
          } else {
            throw new EntityNotFoundException("Item not found with id: " + updatedItemId);
          }
        }
      }

      Order updatedOrder = orderRepository.save(existingOrder);
      return orderMapper.toDto(updatedOrder);
    } else {
      throw new EntityNotFoundException("Order not found with id: " + id);
    }
  }

  public boolean deleteOrder(Long id) {
    Optional<Order> optionalOrder = orderRepository.findById(id);
    if (optionalOrder.isPresent()) {
      Order orderToDelete = optionalOrder.get();
      orderRepository.delete(orderToDelete);
      return true;
    } else {
      return false;
    }
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
