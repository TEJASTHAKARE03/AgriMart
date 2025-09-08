package com.example.demo.config;

import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.model.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create 5 Users
        User user1 = new User("John Doe", "john.doe@example.com", "password123", "CUSTOMER");
        User user2 = new User("Jane Smith", "jane.smith@example.com", "password123", "CUSTOMER");
        User user3 = new User("Peter Jones", "peter.jones@example.com", "password123", "SELLER");
        User user4 = new User("Sarah Miller", "sarah.miller@example.com", "password123", "SELLER");
        User user5 = new User("Michael Brown", "michael.brown@example.com", "password123", "CUSTOMER");

        List<User> users = Arrays.asList(user1, user2, user3, user4, user5);
        userRepository.saveAll(users);

        // Create Sellers for users with SELLER role
        Seller seller1 = new Seller(user3);
        Seller seller2 = new Seller(user4);

        List<Seller> sellers = Arrays.asList(seller1, seller2);
        sellerRepository.saveAll(sellers);

        // Create 5 Products, associated with sellers
        Product product1 = new Product("Laptop", "High-performance laptop", 1200.00, "Electronics", seller1);
        Product product2 = new Product("Smartphone", "Latest model smartphone", 800.00, "Electronics", seller1);
        Product product3 = new Product("Office Chair", "Ergonomic office chair", 250.00, "Furniture", seller2);
        Product product4 = new Product("Desk Lamp", "Modern LED desk lamp", 45.00, "Furniture", seller2);
        Product product5 = new Product("Coffee Maker", "Automatic coffee maker", 150.00, "Appliances", seller2);

        List<Product> products = Arrays.asList(product1, product2, product3, product4, product5);
        productRepository.saveAll(products);

        System.out.println("Loaded " + users.size() + " users, " + sellers.size() + " sellers, and " + products.size() + " products into the database.");
    }
}
