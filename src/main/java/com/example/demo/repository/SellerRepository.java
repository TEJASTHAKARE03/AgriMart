package com.example.demo.repository;

import com.example.demo.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {

    Optional<Seller> findByShopName(String shopName);
}
