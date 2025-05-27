package com.ecom.profile_service.config;

import com.ecom.profile_service.entity.EcomUser;
import com.ecom.profile_service.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EcomUser> credential = repository.findByEmail(username);

        return credential.map(user -> new CustomUserDetails(user))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
