package com.example.demo.service;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.UserRegistrationRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserUpdateProfileDTO;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.model.enums.UserType;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO registerUser(UserRegistrationRequestDTO requestDTO) {
        userRepository.findByEmail(requestDTO.getEmail()).ifPresent(existingUser -> {
            throw new IllegalStateException("Error: Email address '" + requestDTO.getEmail() + "' is already in use.");
        });

        User newUser = new User();
        newUser.setName(requestDTO.getName());
        newUser.setEmail(requestDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        newUser.setPhone(requestDTO.getPhone());
        newUser.setAddress(requestDTO.getAddress());
        newUser.setUserType(UserType.valueOf(requestDTO.getUserType()));

        Cart newUserCart = new Cart();
        newUserCart.setUser(newUser);
        newUser.setCart(newUserCart);

        User savedUser = userRepository.save(newUser);

        return convertToResponseDTO(savedUser);
    }

    public Optional<UserResponseDTO> findUserById(Integer userId) {
        return userRepository.findById(userId).map(this::convertToResponseDTO);
    }

    @Transactional
    public UserResponseDTO updateUserDetails(Integer userId, UserUpdateProfileDTO updateDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with ID " + userId + " not found."));

        existingUser.setName(updateDTO.getName());
        existingUser.setPhone(updateDTO.getPhone());
        existingUser.setAddress(updateDTO.getAddress());

        User updatedUser = userRepository.save(existingUser);
        return convertToResponseDTO(updatedUser);
    }

    @Transactional
    public void changeUserPassword(Integer userId, ChangePasswordRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));

        if (!passwordEncoder.matches(requestDTO.getOldPassword(), user.getPassword())) {
            throw new SecurityException("Incorrect old password.");
        }

        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));

        userRepository.save(user);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setUserType(user.getUserType());
        return dto;
    }
}
