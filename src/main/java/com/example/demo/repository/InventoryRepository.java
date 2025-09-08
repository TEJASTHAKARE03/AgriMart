package com.example.demo.repository;

import com.example.demo.model.Inventory;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByProductOrderByUpdatedAtDesc(Product product);
}
