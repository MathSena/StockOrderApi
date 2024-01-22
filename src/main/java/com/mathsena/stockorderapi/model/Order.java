package com.mathsena.stockorderapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
@Table(name = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime creationDate;
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @JsonManagedReference("order-orderItem")
  private List<OrderItem> orderItems;

  @ManyToOne(fetch = FetchType.EAGER)
  @JsonBackReference("user-order")
  private User user;

  private boolean completed = false;
}

