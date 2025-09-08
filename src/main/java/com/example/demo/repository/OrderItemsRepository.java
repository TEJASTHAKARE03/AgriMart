package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

    List<OrderItems> findByOrder(Order order);
}
