package com.kaitech.student_crm.payload.response;

import java.time.LocalDate;

public record NotificationResponse(
        Long id,
        String message,
        LocalDate date
) {
}