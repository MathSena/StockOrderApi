package com.mathsena.stockorderapi.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
  private Long itemId;
  private Integer quantity;

}
