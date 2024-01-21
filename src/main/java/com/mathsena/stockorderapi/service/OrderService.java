package com.mathsena.stockorderapi.service;

import com.mathsena.stockorderapi.dto.OrderDTO;
import com.mathsena.stockorderapi.model.User;

import java.util.List;

public interface OrderService {
  List<OrderDTO> getAllOrders();

  OrderDTO getOrderById(Long id);


  OrderDTO createOrder(OrderDTO newOrder);

  OrderDTO updateOrder(Long id, OrderDTO updatedCustomerOrderDTO);

  boolean deleteOrder(Long id);

  void sendEmailNotification(User userDTO);
}
