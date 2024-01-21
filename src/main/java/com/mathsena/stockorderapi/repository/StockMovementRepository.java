package com.mathsena.stockorderapi.repository;

import com.mathsena.stockorderapi.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
  List<StockMovement> findByItemId(Long itemId);

  @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.item.id = :itemId")
  int findAvailableByItemId(@Param("itemId") Long itemId);

}
