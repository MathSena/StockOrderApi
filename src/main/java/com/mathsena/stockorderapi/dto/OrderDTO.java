package com.mathsena.stockorderapi.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
  private LocalDateTime creationDate;
  private List<OrderItemDTO> orderItems;
  private Long userId;
  private boolean completed;
}
