package com.kaitech.student_crm.controllers;

import com.kaitech.student_crm.dtos.UserResponse;
import com.kaitech.student_crm.payload.request.LoginRequest;
import com.kaitech.student_crm.payload.request.SignUpRequest;
import com.kaitech.student_crm.payload.response.MessageResponse;
import com.kaitech.student_crm.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/sign/in")
    @Operation(summary = "Аутентификация пользователя по email и паролю")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.signIn(loginRequest);
    }

    @PostMapping("/sign/up")
    @Operation(summary = "Регистрация нового пользователя")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.createUser(signUpRequest);
    }

    @GetMapping("get/code/reset/password/{email}")
    @Operation(summary = "Запрос кода сброса пароля на указанный email")
    public MessageResponse resetCode(@PathVariable String email) {
        return userService.resetPassword(email);
    }

    @PutMapping("reset/password/{email}/{code}")
    @Operation(summary = "Сброс пароля пользователя по email и коду")
    public UserResponse resetPassword(@PathVariable String email,
                                      @PathVariable Integer code,
                                      @RequestParam String password) {
        return userService.newPassword(email, code, password);
    }
}
