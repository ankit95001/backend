package com.ecom.profile_service.service;

import com.ecom.profile_service.dto.MessageDto;
import com.ecom.profile_service.entity.EcomUser;
import com.ecom.profile_service.entity.PasswordResetToken;
import com.ecom.profile_service.exception.CustomException;
import com.ecom.profile_service.feign.MessageClient;
import com.ecom.profile_service.repository.MyUserRepository;
import com.ecom.profile_service.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private MyUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private MessageClient messageClient;

    @Autowired
    private JwtService jwtService;

    public String saveUser(EcomUser credential) {
        if (repository.existsByEmail(credential.getEmail())) {
            throw new CustomException("User already registered with this email");
        }
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "User successfully registered";
    }

    public String generateResetLink(String email) {
        Optional<EcomUser> userOpt = repository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Invalidate old tokens
        tokenRepository.deleteByEmail(email);

        // Create new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiry);
        tokenRepository.save(resetToken);

        // Send reset link via MessageClient
        String resetLink = "http://localhost:8765/auth/reset-password?token=" + token;

        MessageDto message = new MessageDto();
        message.setEmail(email);
        message.setSubject("Reset Your Password");
        message.setMessage("Click the following link to reset your password: " + resetLink);
        messageClient.sendMessage(message);

        return "Password reset link sent to email.";
    }

    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        EcomUser user = repository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);

        tokenRepository.delete(resetToken);

        return "Password has been reset successfully.";
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
