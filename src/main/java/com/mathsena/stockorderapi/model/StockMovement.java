package com.mathsena.stockorderapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class StockMovement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime creationDate;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;

  private Integer quantity;
}
