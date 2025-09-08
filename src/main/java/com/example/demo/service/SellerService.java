package com.example.demo.service;

import com.example.demo.model.Seller;
import com.example.demo.model.User;
import com.example.demo.repository.SellerRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository, UserRepository userRepository) {
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Allows a regular user to register as a seller.
     */
    @Transactional
    public Seller createSellerProfile(Seller seller, Integer userId) {
        // 1. Find the user who is becoming a seller.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with ID " + userId + " not found."));

        // 2. BUSINESS RULE: Check if this user already has a seller profile.
        if (user.getSeller() != null) {
            throw new IllegalStateException("This user is already registered as a seller.");
        }

        // 3. Link the new Seller profile to the User.
        seller.setUser(user);

        // 4. Save the new seller profile.
        return sellerRepository.save(seller);
    }

    /**
     * Finds a seller profile using the associated user's ID.
     */
    public Optional<Seller> getSellerProfileByUserId(Integer userId) {
        return userRepository.findById(userId).map(User::getSeller);
    }

    /**
     * Updates a seller's profile information.
     */
    @Transactional
    public Seller updateSellerProfile(Integer sellerId, Seller updatedInfo) {
        Seller existingSeller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalStateException("Seller profile with ID " + sellerId + " not found."));

        existingSeller.setShopName(updatedInfo.getShopName());
        existingSeller.setGstNumber(updatedInfo.getGstNumber());

        return sellerRepository.save(existingSeller);
    }
}
