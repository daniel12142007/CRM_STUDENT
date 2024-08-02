package com.kaitech.student_crm.payload.request;

import com.kaitech.student_crm.annotations.PasswordMatches;
import com.kaitech.student_crm.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Data
@PasswordMatches
public class SignUpRequest {
    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Password is required")
    @Size(min = 8)
    private String password;
    private String confirmPassword;


    public @Email(message = "It should have email format") @NotBlank(message = "User email is required") String getEmail() {
        return email;
    }
}
