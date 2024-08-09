package com.kaitech.student_crm.payload.request;

import java.time.LocalDate;

public record ExamRequest(
        LocalDate dateStart,
        LocalDate dateEnd,
        String newLevel,
        Long studentId
) {}