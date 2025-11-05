package com.energyhub.authservice.service;

import com.energyhub.authservice.dto.LoginRequest;
import com.energyhub.authservice.dto.LoginResponse;
import com.energyhub.authservice.model.User;
import com.energyhub.authservice.repository.UserRepository;
import com.energyhub.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if password is BCrypt hashed or plain text
        boolean passwordMatches;
        if (user.getPassword().startsWith("$2a$") || user.getPassword().startsWith("$2b$")) {
            // BCrypt hashed password
            passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        } else {
            // Plain text password (for backward compatibility)
            passwordMatches = user.getPassword().equals(loginRequest.getPassword());
        }

        if (!passwordMatches) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().toString(),
                user.getId()
        );

        return new LoginResponse(token, user.getUsername(), user.getRole().toString(), user.getId());
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}