package com.kaitech.student_crm.payload.response;

public record ServicesResponse(
        Long id,
        String title,
        String description,
        Integer price
) {
}