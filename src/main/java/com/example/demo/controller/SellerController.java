package com.example.demo.controller;

import com.example.demo.model.Seller;
import com.example.demo.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * Endpoint for a user to create a seller profile.
     * HTTP Method: POST
     * URL: /api/sellers/register?userId={id}
     */
    @PostMapping("/register")
    public ResponseEntity<?> createSellerProfile(@RequestBody Seller seller, @RequestParam Integer userId) {
        try {
            Seller newSellerProfile = sellerService.createSellerProfile(seller, userId);
            return new ResponseEntity<>(newSellerProfile, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to get a seller's profile using their user ID.
     * HTTP Method: GET
     * URL: /api/sellers/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Seller> getSellerProfileByUserId(@PathVariable Integer userId) {
        return sellerService.getSellerProfileByUserId(userId)
                .map(seller -> new ResponseEntity<>(seller, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint to update a seller's profile.
     * HTTP Method: PUT
     * URL: /api/sellers/{sellerId}
     */
    @PutMapping("/{sellerId}")
    public ResponseEntity<?> updateSellerProfile(@PathVariable Integer sellerId, @RequestBody Seller updatedInfo) {
        try {
            Seller updatedSeller = sellerService.updateSellerProfile(sellerId, updatedInfo);
            return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
