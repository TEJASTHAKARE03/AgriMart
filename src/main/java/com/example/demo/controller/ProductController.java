package com.example.demo.controller;

import com.example.demo.dto.ProductCreateRequestDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint to create a new product.
     * The seller ID is now part of the request body DTO.
     * HTTP Method: POST
     * URL: /api/products
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequestDTO requestDTO) {
        try {
            ProductResponseDTO createdProduct = productService.createProduct(requestDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to get all products or filter by category or name.
     * HTTP Method: GET
     * URL: /api/products
     * URL (filtered): /api/products?category=Electronics
     * URL (filtered): /api/products?name=Laptop
     */
    @GetMapping
    public List<ProductResponseDTO> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {
        if (category != null) {
            return productService.findProductsByCategory(category);
        } else if (name != null) {
            return productService.findProductsByName(name);
        } else {
            return productService.findAllProducts();
        }
    }

    /**
     * Endpoint to find a product by its ID.
     * HTTP Method: GET
     * URL: /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Integer id) {
        return productService.findProductById(id)
                .map(productDTO -> new ResponseEntity<>(productDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
