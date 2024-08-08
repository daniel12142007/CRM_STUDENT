package com.kaitech.student_crm.payload.response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public record ArchiveResponse(
        Long id,
        LocalDate dateUpdate,
        String newLevel,
        String oldLevel,
        Boolean status,
        StudentResponse student
) {}
