package com.mathsena.stockorderapi.controller;

import com.mathsena.stockorderapi.dto.OrderDTO;
import com.mathsena.stockorderapi.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/api/orders")
@Api(tags = "Order Controller", description = "Endpoints for managing orders")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  @ApiOperation("Get a list of all orders")
  @ApiResponse(code = 200, message = "List of orders retrieved successfully")
  public ResponseEntity<List<OrderDTO>> getAllOrders() {
    List<OrderDTO> orders = orderService.getAllOrders();
    return ResponseEntity.ok(orders);
  }

  @PostMapping
  @ApiOperation("Create a new order")
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Order created successfully"),
          @ApiResponse(code = 400, message = "Invalid request body")
  })
  public ResponseEntity<Object> createOrder(@RequestBody OrderDTO newOrderDTO) {
    OrderDTO createdOrderDTO = orderService.createOrder(newOrderDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDTO);
  }

  @GetMapping("/{id}")
  @ApiOperation("Get an order by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Order retrieved successfully"),
          @ApiResponse(code = 404, message = "Order not found")
  })
  public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
    OrderDTO order = orderService.getOrderById(id);
    if (order != null) {
      return ResponseEntity.ok(order);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found!");
    }
  }

  @PutMapping("/{id}")
  @ApiOperation("Update an order by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Order updated successfully"),
          @ApiResponse(code = 400, message = "Invalid request body"),
          @ApiResponse(code = 404, message = "Order not found")
  })
  public ResponseEntity<Object> updateOrder(
          @PathVariable Long id, @RequestBody OrderDTO updatedOrderDTO) {
    OrderDTO order = orderService.updateOrder(id, updatedOrderDTO);
    if (order != null) {
      return ResponseEntity.ok(order);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found!");
    }
  }

  @DeleteMapping("/{id}")
  @ApiOperation("Delete an order by ID")
  @ApiResponses(value = {
          @ApiResponse(code = 204, message = "Order deleted successfully"),
          @ApiResponse(code = 404, message = "Order not found")
  })
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    boolean deleted = orderService.deleteOrder(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
