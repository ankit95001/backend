package com.ecom.profile_service.controller;

import com.ecom.profile_service.entity.EcomUser;
import com.ecom.profile_service.entity.UserContext;
import com.ecom.profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserContext userContext;

    private void validateAdminRole() {
        if (!"ADMIN".equalsIgnoreCase(userContext.getRole())) {
            throw new SecurityException("Unauthorized: Admin role required");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EcomUser> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getUserById(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EcomUser>> getAllUsers(){
        validateAdminRole();
        return ResponseEntity.ok(userProfileService.getAllUsers());
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
