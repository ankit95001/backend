package com.ecom.profile_service.service;

import com.ecom.profile_service.dto.EcomUserUpdateRequest;
import com.ecom.profile_service.entity.Address;
import com.ecom.profile_service.entity.EcomUser;
import com.ecom.profile_service.entity.UserContext;
import com.ecom.profile_service.repository.MyUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired
    private MyUserRepository userRepository;


    public EcomUser getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }


    @Transactional
    public EcomUser updateProfile(Long userId, EcomUserUpdateRequest request) {
        EcomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setFullName(request.getFullName());
        user.setMobile(request.getMobile());
        user.setAbout(request.getAbout());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());

        if (request.getAddresses() != null) {
            List<Address> existingAddresses = user.getAddresses();
            Map<Long, Address> existingMap = existingAddresses.stream()
                    .filter(addr -> addr.getId() != null)
                    .collect(Collectors.toMap(Address::getId, a -> a));

            List<Address> finalAddressList = new ArrayList<>();

            for (Address incoming : request.getAddresses()) {
                if (incoming.getId() != null && existingMap.containsKey(incoming.getId())) {
                    // Update existing address
                    Address existing = existingMap.get(incoming.getId());
                    existing.setName(incoming.getName());
                    existing.setLocality(incoming.getLocality());
                    existing.setAddress(incoming.getAddress());
                    existing.setCity(incoming.getCity());
                    existing.setState(incoming.getState());
                    existing.setPinCode(incoming.getPinCode());
                    existing.setMobile(incoming.getMobile());
                    finalAddressList.add(existing);
                } else {
                    // New address (no ID)
                    finalAddressList.add(incoming);
                }
            }

            user.getAddresses().clear(); // Clear the collection to avoid orphan conflict
            user.getAddresses().addAll(finalAddressList); // Add updated + new addresses
        }

        return userRepository.save(user);
    }


    public List<EcomUser> getAllUsers() {
        return userRepository.findAll();
    }
}
