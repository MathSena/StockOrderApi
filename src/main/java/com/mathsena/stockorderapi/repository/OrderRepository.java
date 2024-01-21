package com.mathsena.stockorderapi.repository;

import com.mathsena.stockorderapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE oi.item.id = :itemId AND o.completed = false")
    List<Order> findPendingOrdersByItemId(@Param("itemId") Long itemId);

}
