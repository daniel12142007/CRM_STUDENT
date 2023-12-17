package com.example.ecomarket.service;

import com.example.ecomarket.config.JwtUtils;
import com.example.ecomarket.dto.response.RegisterResponse;
import com.example.ecomarket.model.User;
import com.example.ecomarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse user() {
        User user = new User();
        userRepository.save(user);
        User user1 = userRepository.findById(user.getId()).get();
        user1.setPassword(passwordEncoder.encode(String.valueOf(user.getId())));
        user1.setEmail(String.valueOf(user.getId()));
        userRepository.save(user1);
        String token = jwtUtils.generateToken(user1.getEmail());
        return RegisterResponse.builder()
                .id(user1.getId())
                .token(token)
                .build();
    }
}