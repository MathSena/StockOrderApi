package com.mathsena.stockorderapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
  private Long id;
  private LocalDateTime creationDate;
  private List<OrderItemDTO> orderItems;
  private Long userId;
  private boolean completed;
}
