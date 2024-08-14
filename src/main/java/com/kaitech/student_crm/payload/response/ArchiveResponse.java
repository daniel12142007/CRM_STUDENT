package com.kaitech.student_crm.payload.response;

import java.time.LocalDate;

public record ArchiveResponse(
        Long id,
        LocalDate dateUpdate,
        String newLevel,
        String oldLevel,
        Integer oldPoint,
        Integer newPoint,
        String image,
        String firstName,
        String lastName,
        String email
) {
}