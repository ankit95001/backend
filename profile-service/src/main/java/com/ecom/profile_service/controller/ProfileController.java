package com.ecom.profile_service.controller;

import com.ecom.profile_service.dto.AuthRequest;
import com.ecom.profile_service.dto.LoginResponse;
import com.ecom.profile_service.entity.EcomUser;
import com.ecom.profile_service.repository.MyUserRepository;
import com.ecom.profile_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Product Controller", description = "Manage product operations")
public class ProfileController {

    @Autowired
    private AuthService service;

    @Autowired
    MyUserRepository repository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody EcomUser user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user));
    }

    @Operation(summary = "Login and get JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Validation errors")
    })
    @PostMapping("/login")
    public ResponseEntity<?> getToken(@Valid @RequestBody AuthRequest authRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            if (auth.isAuthenticated()) {
                Optional<EcomUser> user = repository.findByEmail(authRequest.getEmail());
                if (user.isPresent()) {
                    EcomUser ecomUser = user.get();
                    String token = service.generateToken(ecomUser.getEmail());
                    LoginResponse response = new LoginResponse(token, ecomUser.getEmail(), ecomUser.getId(),ecomUser.getRole().name());
                    System.out.println(response);
                    return ResponseEntity.ok(response);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }


    @Operation(summary = "Validate JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "400", description = "Token is invalid")
    })
    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            String result = service.generateResetLink(email);
            return ResponseEntity.ok(result);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @RequestParam String newPassword) {
        try {
            String result = service.resetPassword(token, newPassword);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}