package com.example.ecomarket.api;

import com.example.ecomarket.dto.response.RegisterResponse;
import com.example.ecomarket.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class MeniApi {
    private final AuthService authService;
    @GetMapping("/menu")
    public RegisterResponse hello(){
        return authService.user();
    }
}
