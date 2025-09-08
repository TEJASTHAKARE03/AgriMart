package com.example.demo.repository;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItems;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {

    Optional<CartItems> findByCartAndProduct(Cart cart, Product product);
}
