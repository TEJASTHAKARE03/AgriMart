package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint to create a new order from a user's cart.
     * HTTP Method: POST
     * URL: /api/orders/{userId}/create
     */
    @PostMapping("/{userId}/create")
    public ResponseEntity<?> createOrderFromCart(@PathVariable Integer userId) {
        try {
            Order newOrder = orderService.createOrderFromCart(userId);
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to get all orders for a specific user.
     * HTTP Method: GET
     * URL: /api/orders/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrdersForUser(@PathVariable Integer userId) {
        try {
            List<Order> orders = orderService.findOrdersForUser(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
