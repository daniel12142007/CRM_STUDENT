package com.kaitech.student_crm.payload.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String message,
        LocalDateTime date
) {
}