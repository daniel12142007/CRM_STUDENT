package com.example.ecomarket.service;

import com.example.ecomarket.config.JwtUtils;
import com.example.ecomarket.dto.request.RegisterUserRequest;
import com.example.ecomarket.dto.response.JWTResponse;
import com.example.ecomarket.model.Basket;
import com.example.ecomarket.model.User;
import com.example.ecomarket.model.enums.Role;
import com.example.ecomarket.repository.BasketRepository;
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
    private final BasketRepository basketRepository;

    public JWTResponse register(RegisterUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("found email:" + request.getEmail() + " email");
        userRepository.save(user);
        Basket basket = new Basket();
        basket.setUser(user);
        basketRepository.save(basket);
        String token = jwtUtils.generateToken(user.getEmail());
        return new JWTResponse(
                user.getEmail(),
                token,
                "login",
                user.getRole()
        );
    }

    public JWTResponse login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new RuntimeException("not found:" + email + " email");
        });
        System.out.println(user.getPassword());
        System.out.println(passwordEncoder.encode(password));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtUtils.generateToken(user.getEmail());
        return new JWTResponse(
                user.getEmail(),
                token,
                "login",
                user.getRole()
        );
    }
}