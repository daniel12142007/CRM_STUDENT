package com.kaitech.student_crm.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record StudentRequest(
        @NotEmpty(message = "Image cannot be empty") String image,
        @NotEmpty(message = "First name cannot be empty") String firstName,
        @NotEmpty(message = "Last name cannot be empty") String lastName,
        @NotEmpty(message = "Email cannot be empty") @Email(message = "It should have email format") String email,
        @NotEmpty(message = "Phone number cannot be empty") String phoneNumber,
        @NotNull(message = "Direction cannot be null") Long direction,
        @NotNull(message = "Point cannot be null") Integer point
) {
}