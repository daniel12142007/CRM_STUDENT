package com.kaitech.student_crm.payload.response;

import java.time.LocalDate;

public record ExamResponse(
        Long id,
        LocalDate dateStart,
        LocalDate dateEnd,
        String newLevel,
        StudentResponse student
) {}

