package com.ecom.profile_service.service;

import com.ecom.profile_service.entity.EcomUser;
import com.ecom.profile_service.entity.UserContext;
import com.ecom.profile_service.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private MyUserRepository userRepository;


    public EcomUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }


    public EcomUser updateUser(Long userId, EcomUser updatedUser) {
        EcomUser existingUser = getUserById(userId);

        if (updatedUser.getFullName() != null) {
            existingUser.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getMobile() != null) {
            existingUser.setMobile(updatedUser.getMobile());
        }
        if (updatedUser.getImage() != null) {
            existingUser.setImage(updatedUser.getImage());
        }
        if (updatedUser.getAbout() != null) {
            existingUser.setAbout(updatedUser.getAbout());
        }
        if (updatedUser.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        }
        if (updatedUser.getGender() != null) {
            existingUser.setGender(updatedUser.getGender());
        }
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }
        if (updatedUser.getAddresses() != null && !updatedUser.getAddresses().isEmpty()) {
            existingUser.setAddresses(updatedUser.getAddresses());
        }

        return userRepository.save(existingUser);
    }

    public List<EcomUser> getAllUsers() {
        return userRepository.findAll();
    }
}
