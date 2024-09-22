package com.kaitech.student_crm.payload.response;

import java.time.LocalDateTime;

public record ArchiveResponse(
        Long id,
        LocalDateTime dateUpdate,
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