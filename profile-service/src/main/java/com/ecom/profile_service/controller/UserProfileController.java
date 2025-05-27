package com.ecom.profile_service.controller;

import com.ecom.profile_service.entity.EcomUser;
import com.ecom.profile_service.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EcomUser> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getUserById(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<EcomUser> updateUserProfile(@PathVariable Long userId, @RequestBody EcomUser updatedUser) {
        return ResponseEntity.ok(userProfileService.updateUser(userId, updatedUser));
    }

    @GetMapping("orders/{userId}")
    public ResponseEntity<?> getOrder(@PathVariable Long userId){
        //To be implemented

        return ResponseEntity.ok("To be implemented later");
    }
}
