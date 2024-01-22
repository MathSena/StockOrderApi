package com.mathsena.stockorderapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
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
  private LocalDateTime creationDate;
  @ManyToOne(fetch = FetchType.EAGER)
  @JsonIgnore
  private Item item;
  private Integer quantity;

}
