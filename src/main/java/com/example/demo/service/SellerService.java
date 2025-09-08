package com.example.demo.service;

import com.example.demo.dto.SellerCreateRequestDTO;
import com.example.demo.dto.SellerResponseDTO;
import com.example.demo.dto.SellerUpdateDTO;
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

    @Transactional
    public SellerResponseDTO createSellerProfile(SellerCreateRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalStateException("User with ID " + requestDTO.getUserId() + " not found."));

        if (user.getSeller() != null) {
            throw new IllegalStateException("This user is already registered as a seller.");
        }

        Seller newSeller = new Seller();
        newSeller.setUser(user);
        newSeller.setShopName(requestDTO.getShopName());
        newSeller.setGstNumber(requestDTO.getGstNumber());

        Seller savedSeller = sellerRepository.save(newSeller);
        return convertToResponseDTO(savedSeller);
    }

    public Optional<SellerResponseDTO> getSellerProfileByUserId(Integer userId) {
        return userRepository.findById(userId)
                .map(User::getSeller)
                .map(this::convertToResponseDTO);
    }

    @Transactional
    public SellerResponseDTO updateSellerProfile(Integer sellerId, SellerUpdateDTO updateDTO) {
        Seller existingSeller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalStateException("Seller profile with ID " + sellerId + " not found."));

        existingSeller.setShopName(updateDTO.getShopName());
        existingSeller.setGstNumber(updateDTO.getGstNumber());

        Seller updatedSeller = sellerRepository.save(existingSeller);
        return convertToResponseDTO(updatedSeller);
    }

    private SellerResponseDTO convertToResponseDTO(Seller seller) {
        if (seller == null) {
            return null;
        }
        SellerResponseDTO dto = new SellerResponseDTO();
        dto.setSellerId(seller.getSellerId());
        dto.setShopName(seller.getShopName());
        dto.setRating(seller.getRating());
        dto.setMemberSince(seller.getCreatedAt());

        if (seller.getUser() != null) {
            dto.setUserId(seller.getUser().getUserId());
            dto.setUserName(seller.getUser().getName());
        }

        return dto;
    }
}
