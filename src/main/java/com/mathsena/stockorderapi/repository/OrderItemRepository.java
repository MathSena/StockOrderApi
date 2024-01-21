package com.mathsena.stockorderapi.repository;

import com.mathsena.stockorderapi.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    @Query("SELECT SUM(oi.allocatedQuantity) FROM OrderItem oi WHERE oi.item.id = :itemId AND oi.order.completed = false")
    Integer findTotalOrderedQuantityByItemId(@Param("itemId") Long itemId);

}
