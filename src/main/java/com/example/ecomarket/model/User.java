package com.example.ecomarket.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final LocalDateTime dataRegister = LocalDateTime.now();
    private String email;
    private String password;
    @OneToOne(mappedBy = "user")
    private Basket basket;
}