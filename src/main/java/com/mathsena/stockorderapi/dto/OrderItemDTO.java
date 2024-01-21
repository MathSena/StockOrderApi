package com.mathsena.stockorderapi.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Long itemId;
    private Integer quantity;

}
