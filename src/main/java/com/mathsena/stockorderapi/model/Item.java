package com.mathsena.stockorderapi.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
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
