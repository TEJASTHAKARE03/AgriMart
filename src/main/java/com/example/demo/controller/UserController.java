package com.example.demo.controller;

import com.example.demo.dto.ChangePasswordRequestDTO;
import com.example.demo.dto.UserRegistrationRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserUpdateProfileDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequestDTO requestDTO) {
        try {
            UserResponseDTO registeredUser = userService.registerUser(requestDTO);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Integer id) {
        return userService.findUserById(id)
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint to update a user's profile information.
     * HTTP Method: PUT
     * URL: /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserDetails(@PathVariable Integer id, @RequestBody UserUpdateProfileDTO updateDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUserDetails(id, updateDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint to change a user's password.
     * HTTP Method: PUT
     * URL: /api/users/{id}/password
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable Integer id, @RequestBody ChangePasswordRequestDTO requestDTO) {
        try {
            userService.changeUserPassword(id, requestDTO);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
