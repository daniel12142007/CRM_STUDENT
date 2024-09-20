package com.kaitech.student_crm.payload.request;

import javax.validation.constraints.NotEmpty;

public record UpdateStudentRequest(
        @NotEmpty(message = "First name cannot be empty") String firstName,
        @NotEmpty(message = "Last name cannot be empty") String lastName,
        @NotEmpty(message = "Phone number cannot be empty") String phoneNumber
) {
}
