package com.mathsena.stockorderapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  @JsonBackReference("order-orderItem")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "item_id")
  @JsonBackReference
  private Item item;

  @Column(nullable = false)
  private Integer quantity;

  private Integer allocatedQuantity = 0;
}
