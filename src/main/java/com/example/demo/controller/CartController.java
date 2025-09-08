package com.example.demo.controller;

import com.example.demo.dto.AddItemToCartRequestDTO;
import com.example.demo.dto.CartDetailDTO;
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
    public ResponseEntity<CartDetailDTO> getCartByUserId(@PathVariable Integer userId) {
        CartDetailDTO cart = cartService.getCartByUserId(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Endpoint to add a product to a user's cart.
     * The request now uses a DTO in the request body.
     * HTTP Method: POST
     * URL: /api/carts/{userId}/add
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDetailDTO> addProductToCart(@PathVariable Integer userId, @RequestBody AddItemToCartRequestDTO requestDTO) {
        try {
            CartDetailDTO updatedCart = cartService.addProductToCart(userId, requestDTO);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (IllegalStateException e) {
            // It's better to have a global exception handler, but for now, this is fine.
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to remove a product from a user's cart.
     * The productId is now a path variable for standard REST practice.
     * HTTP Method: DELETE
     * URL: /api/carts/{userId}/items/{productId}
     */
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDetailDTO> removeProductFromCart(@PathVariable Integer userId, @PathVariable Integer productId) {
        try {
            CartDetailDTO updatedCart = cartService.removeProductFromCart(userId, productId);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
