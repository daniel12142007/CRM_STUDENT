package com.kaitech.student_crm.payload.request;

import javax.validation.constraints.Size;

public record StudentRegisterRequest(
        String firstName,
        String lastName,
        Long directionId,
        @Size(min = 6, message = "The password size must be at least 8 letters") String phoneNumber,
        String passwordOne,
        @Size(min = 6, message = "The password size must be at least 8 letters")
        String passwordTwo
) {
}