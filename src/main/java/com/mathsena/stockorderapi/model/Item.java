package com.mathsena.stockorderapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "item")
  private List<OrderItem> orderItems;

  @OneToMany(mappedBy = "item")
  private List<StockMovement> stockMovements;
}
