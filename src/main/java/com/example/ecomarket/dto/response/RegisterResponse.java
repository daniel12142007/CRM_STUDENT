package com.example.springbootjwtsecuritytask.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String name;
    private String role;
    private LocalDateTime data_Register;

}