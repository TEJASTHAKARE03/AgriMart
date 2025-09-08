package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // Marks this class as a Spring Service, making it a candidate for dependency injection.
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection: Spring will automatically pass instances of our repository and password encoder.
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Implements the business logic for registering a new user.
     * This method is transactional, meaning all database operations within it must complete successfully,
     * or none of them will be persisted.
     */
    @Transactional
    public User registerUser(User user) {
        // 1. BUSINESS RULE: Check if a user with this email already exists.
        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new IllegalStateException("Error: Email address '" + user.getEmail() + "' is already in use.");
        });

        // 2. SECURITY LOGIC: Encrypt the user's plain-text password before saving.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. ORCHESTRATION: Create a new empty cart for the new user.
        Cart newUserCart = new Cart();
        newUserCart.setUser(user); // Link cart to user
        user.setCart(newUserCart); // Link user to cart (establishes the bidirectional link)

        // 4. PERSISTENCE: Save the new user to the database.
        // Because of CascadeType.ALL and the bidirectional link,
        // this single save operation will also automatically save the new cart.
        return userRepository.save(user);
    }

    /**
     * Finds a user by their ID.
     */
    public Optional<User> findUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    /**
     * Implements the business logic for updating a user's profile details.
     */
    @Transactional
    public User updateUserDetails(Integer userId, User updatedInfo) {
        // 1. Find the existing user.
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with ID " + userId + " not found."));

        // 2. Update the user's details.
        // We only update the fields that are safe and intended to be changed here.
        existingUser.setName(updatedInfo.getName());
        existingUser.setPhone(updatedInfo.getPhone());
        existingUser.setAddress(updatedInfo.getAddress());

        // 3. Save the updated user.
        return userRepository.save(existingUser);
    }

    /**
     * Implements the business logic for securely changing a user's password.
     */
    @Transactional
    public void changeUserPassword(Integer userId, String oldPassword, String newPassword) {
        // 1. Find the user.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));

        // 2. SECURITY: Verify the old password.
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new SecurityException("Incorrect old password.");
        }

        // 3. SECURITY: Encode and set the new password.
        user.setPassword(passwordEncoder.encode(newPassword));

        // 4. Save the user with the new password.
        userRepository.save(user);
    }
}
