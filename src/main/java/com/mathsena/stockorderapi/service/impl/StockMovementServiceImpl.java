package com.mathsena.stockorderapi.service.impl;

import com.mathsena.stockorderapi.dto.StockMovementDTO;
import com.mathsena.stockorderapi.mappers.StockMovementMapper;
import com.mathsena.stockorderapi.model.Item;
import com.mathsena.stockorderapi.model.Order;
import com.mathsena.stockorderapi.model.OrderItem;
import com.mathsena.stockorderapi.model.StockMovement;
import com.mathsena.stockorderapi.repository.ItemRepository;
import com.mathsena.stockorderapi.repository.OrderItemRepository;
import com.mathsena.stockorderapi.repository.OrderRepository;
import com.mathsena.stockorderapi.repository.StockMovementRepository;
import com.mathsena.stockorderapi.service.StockMovementService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMovementServiceImpl implements StockMovementService {
  private final StockMovementRepository stockMovementRepository;
  private final StockMovementMapper stockMovementMapper;
  private final ItemRepository itemRepository;
    private final LoggingService loggingService;

  private final OrderRepository orderRepository;

  public StockMovementServiceImpl(
          StockMovementRepository stockMovementRepository,
          StockMovementMapper stockMovementMapper,
          ItemRepository itemRepository,
          OrderItemRepository orderItemRepository, LoggingService loggingService,
          OrderRepository orderRepository) {
    this.stockMovementRepository = stockMovementRepository;
    this.stockMovementMapper = stockMovementMapper;
    this.itemRepository = itemRepository;
      this.loggingService = loggingService;
      this.orderRepository = orderRepository;
  }

  @Override
  public List<StockMovement> listAllStockMovements() {
    return stockMovementRepository.findAll();
  }

  @Override
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

      // Verifique se o pedido está agora completo
      checkAndCompleteOrder(order);

      if (quantity <= 0) break;
    }
  }

  private void checkAndCompleteOrder(Order order) {
    boolean isComplete =
        order.getOrderItems().stream()
            .allMatch(item -> item.getQuantity().equals(item.getAllocatedQuantity()));
    if (isComplete) {
      order.setCompleted(true);
      // Aqui, você pode implementar a lógica de envio de notificação por email
      // sendEmailNotification(order.getUser());
    }
    orderRepository.save(order);
  }

  @Override
  public StockMovement updateStockMovement(StockMovement stockMovement) {
    if (stockMovement.getId() != null
        && stockMovementRepository.existsById(stockMovement.getId())) {
      return stockMovementRepository.save(stockMovement);
    }
    return null;
  }

  @Override
  public boolean deleteStockMovement(Long id) {
    if (stockMovementRepository.existsById(id)) {
      stockMovementRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public List<StockMovement> listStockMovementsByProductId(Long itemId) {
    return stockMovementRepository.findByItemId(itemId);
  }

}
