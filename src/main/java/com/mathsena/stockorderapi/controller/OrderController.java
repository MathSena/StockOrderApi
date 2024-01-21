package com.mathsena.stockorderapi.controller;

import com.mathsena.stockorderapi.dto.OrderDTO;
import com.mathsena.stockorderapi.service.OrderService;
import com.mathsena.stockorderapi.service.impl.LoggingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/orders")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<OrderDTO>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @PostMapping
  public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO newOrderDTO) {
    OrderDTO createdOrderDTO = orderService.createOrder(newOrderDTO);
    return new ResponseEntity<>(createdOrderDTO, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
    OrderDTO order = orderService.getOrderById(id);
    if (order != null) {
      return ResponseEntity.ok(order);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderDTO> updateOrder(
      @PathVariable Long id, @RequestBody OrderDTO updatedOrderDTO) {
    OrderDTO order = orderService.updateOrder(id, updatedOrderDTO);
    if (order != null) {
      return ResponseEntity.ok(order);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    if (orderService.deleteOrder(id)) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
