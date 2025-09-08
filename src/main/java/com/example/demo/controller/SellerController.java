package com.example.demo.controller;

import com.example.demo.dto.SellerCreateRequestDTO;
import com.example.demo.dto.SellerResponseDTO;
import com.example.demo.dto.SellerUpdateDTO;
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
     * The request now uses a clean DTO.
     * HTTP Method: POST
     * URL: /api/sellers/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> createSellerProfile(@RequestBody SellerCreateRequestDTO requestDTO) {
        try {
            SellerResponseDTO newSellerProfile = sellerService.createSellerProfile(requestDTO);
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
    public ResponseEntity<SellerResponseDTO> getSellerProfileByUserId(@PathVariable Integer userId) {
        return sellerService.getSellerProfileByUserId(userId)
                .map(sellerDTO -> new ResponseEntity<>(sellerDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint to update a seller's profile.
     * HTTP Method: PUT
     * URL: /api/sellers/{sellerId}
     */
    @PutMapping("/{sellerId}")
    public ResponseEntity<?> updateSellerProfile(@PathVariable Integer sellerId, @RequestBody SellerUpdateDTO updateDTO) {
        try {
            SellerResponseDTO updatedSeller = sellerService.updateSellerProfile(sellerId, updateDTO);
            return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
