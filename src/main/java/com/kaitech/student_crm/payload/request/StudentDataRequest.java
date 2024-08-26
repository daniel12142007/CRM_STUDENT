package com.kaitech.student_crm.payload.request;

import com.kaitech.student_crm.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
@Data
public class StudentDataRequest {
    @Email(message = "It should have email format")
    @NotBlank(message = "Student email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Please enter student's name")
    private String firstname;

    @NotEmpty(message = "Please enter student's lastname")
    private String lastname;

    @NotEmpty(message = "Please enter student's phone number")
    private String phoneNumber;

    public @Email(message = "It should have email format") @NotBlank(message = "Student email is required") String getEmail() {
        return email;
    }
}