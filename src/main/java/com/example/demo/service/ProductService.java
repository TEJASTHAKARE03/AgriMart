package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    /**
     * Business logic to create a new product and associate it with a seller.
     *
     * @param product the product to be created
     * @param sellerId the ID of the seller who owns this product
     * @return the newly created and saved Product
     * @throws IllegalStateException if the seller does not exist
     */
    @Transactional
    public Product createProduct(Product product, Integer sellerId) {
        // 1. BUSINESS RULE: A product must be linked to a valid seller.
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalStateException("Error: Seller with ID " + sellerId + " not found. Cannot create product."));

        // 2. BUSINESS LOGIC: Associate the product with its seller.
        product.setSeller(seller);

        // 3. DATA ACCESS: Save the new product.
        return productRepository.save(product);
    }

    /**
     * Finds a product by its ID.
     */
    public Optional<Product> findProductById(Integer id) {
        return productRepository.findById(id);
    }

    /**
     * Finds all products in a given category.
     */
    public List<Product> findProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Finds products whose names contain the given search term (case-insensitive).
     */
    public List<Product> findProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves all products.
     */
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
