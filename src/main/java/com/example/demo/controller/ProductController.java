package com.example.demo.controller;

import com.example.demo.model.Product;
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
     * The seller ID must be passed as a request parameter.
     * HTTP Method: POST
     * URL: /api/products?sellerId={id}
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product, @RequestParam Integer sellerId) {
        try {
            Product createdProduct = productService.createProduct(product, sellerId);
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
    public List<Product> getAllProducts(
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
    public ResponseEntity<Product> findProductById(@PathVariable Integer id) {
        return productService.findProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
