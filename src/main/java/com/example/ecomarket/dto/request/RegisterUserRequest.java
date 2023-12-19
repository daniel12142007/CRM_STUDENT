package com.example.ecomarket.dto.request;

import lombok.Data;

import javax.validation.constraints.*;
@Data
public class RegisterUserRequest {
    @NotNull(message = "Your fullName it is null!")
    @NotEmpty(message = "Your fullName it is empty!")
    private String fullName;
    @NotNull(message = "Your email it is null!")
    @NotEmpty(message = "Your email it is empty!")
    private String email;
    @NotNull(message = "Your password it is null!")
    @NotBlank(message = "Your password it is blank!")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "The password does not meet the conditions")
    @Size(min = 8, message = "The line length must be at least 8 characters.")
    private String password;

    public void setEmail(String email) {
        if (email != null && email.endsWith("@gmail.com")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Некорректный email. Должен оканчиваться на @gmail.com");
        }
    }
}