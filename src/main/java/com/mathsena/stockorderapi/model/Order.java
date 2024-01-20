package com.mathsena.stockorderapi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "customer_order")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime creationDate;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> orderItems;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
