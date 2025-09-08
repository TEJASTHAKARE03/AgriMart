package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // Base path for all user-related endpoints
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to register a new user.
     * HTTP Method: POST
     * URL: /api/users/register
     * Body: JSON representation of the User object
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            // On success, return the created user and an HTTP 201 Created status.
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // If the service throws an exception (e.g., email in use), return an HTTP 400 Bad Request status.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint to find a user by their ID.
     * HTTP Method: GET
     * URL: /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Integer id) {
        return userService.findUserById(id)
                // If found, wrap it in a ResponseEntity with an HTTP 200 OK status.
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                // If not found, return an HTTP 404 Not Found status.
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
