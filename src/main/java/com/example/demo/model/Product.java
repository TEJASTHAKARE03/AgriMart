package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    private String name;

    @Lob
    private String description;

    private String category;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private int stock;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Product(String name, String description, Double price, String category, Seller seller) {
        this.name = name;
        this.description = description;
        this.price = BigDecimal.valueOf(price);
        this.category = category;
        this.seller = seller;
        this.stock = 100; // Default stock
    }
}