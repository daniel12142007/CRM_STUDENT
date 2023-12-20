package com.example.ecomarket.dto.response;

import com.example.ecomarket.model.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JWTResponse {
    private String email;
    private String token;
    private String message;
    private Role role;
}