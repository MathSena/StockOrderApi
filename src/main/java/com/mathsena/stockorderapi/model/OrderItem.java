package com.mathsena.stockorderapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;

  private Integer quantity;
}
