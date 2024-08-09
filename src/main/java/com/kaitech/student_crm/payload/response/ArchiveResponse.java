package com.kaitech.student_crm.payload.response;

import java.time.LocalDate;

public record ArchiveResponse(
        Long id,
        LocalDate dateUpdate,
        String newLevel,
        String oldLevel,
        Boolean status,
        Long studentId,
        String image,
        String firstName,
        String lastName,
        String email
) {}