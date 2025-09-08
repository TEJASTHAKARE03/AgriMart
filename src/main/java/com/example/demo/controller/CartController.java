package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Endpoint to get the cart for a specific user.
     * HTTP Method: GET
     * URL: /api/carts/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Integer userId) {
        try {
            Cart cart = cartService.getCartByUserId(userId);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to add a product to a user's cart.
     * HTTP Method: POST
     * URL: /api/carts/{userId}/add?productId={pid}&quantity={qty}
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addProductToCart(@PathVariable Integer userId, @RequestParam Integer productId, @RequestParam int quantity) {
        try {
            Cart updatedCart = cartService.addProductToCart(userId, productId, quantity);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to remove a product from a user's cart.
     * HTTP Method: DELETE
     * URL: /api/carts/{userId}/remove?productId={pid}
     */
    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<?> removeProductFromCart(@PathVariable Integer userId, @RequestParam Integer productId) {
        try {
            cartService.removeProductFromCart(userId, productId);
            return new ResponseEntity<>("Product removed from cart successfully.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
