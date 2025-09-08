package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.OrderTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTrackingRepository extends JpaRepository<OrderTracking, Integer> {

    List<OrderTracking> findByOrderOrderByUpdatedAtDesc(Order order);
}
