package com.example.ecomarket.api;

import com.example.ecomarket.dto.request.RegisterUserRequest;
import com.example.ecomarket.dto.response.JWTResponse;
import com.example.ecomarket.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthApi {
    private final AuthService authService;

    @PostMapping("/register")
    @PermitAll
    public JWTResponse register(
            @RequestBody @Valid RegisterUserRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @PermitAll
    public JWTResponse login(@RequestParam
                             @Pattern(regexp = ".*@gmail.com$")
                             @NotEmpty(message = "It is empty")
                             String email,
                             @RequestParam
                             @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "The password does not meet the conditions")
                             @Size(min = 8, message = "The line length must be at least 8 characters.")
                             @NotEmpty(message = "It is empty")
                             String password) {
        return authService.login(email, password);
    }
}
