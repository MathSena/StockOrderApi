package com.mathsena.stockorderapi.dto;

import lombok.Data;

@Data
public class StockMovementDTO {
  private Long id;
  private Long itemId;
  private Integer quantity;
}
