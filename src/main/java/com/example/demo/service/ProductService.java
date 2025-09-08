package com.example.demo.service;

import com.example.demo.dto.ProductCreateRequestDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductCreateRequestDTO requestDTO) {
        Seller seller = sellerRepository.findById(requestDTO.getSellerId())
                .orElseThrow(() -> new IllegalStateException("Error: Seller with ID " + requestDTO.getSellerId() + " not found. Cannot create product."));

        Product product = new Product();
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setCategory(requestDTO.getCategory());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        product.setSeller(seller);

        Product savedProduct = productRepository.save(product);

        return convertToResponseDTO(savedProduct);
    }

    public Optional<ProductResponseDTO> findProductById(Integer id) {
        return productRepository.findById(id).map(this::convertToResponseDTO);
    }

    public List<ProductResponseDTO> findProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> findProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        if (product.getSeller() != null) {
            dto.setSellerId(product.getSeller().getSellerId());
            dto.setSellerShopName(product.getSeller().getShopName());
        }

        return dto;
    }
}
