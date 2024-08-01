package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.models.enums.ProjectType;

import java.time.LocalDate;
import java.util.List;


public record ProjectResponse(
        Long id,
        String title,
        String description,
        ProjectType projectType,
        List<StudentResponse> students,
        LocalDate startDate,
        LocalDate endDate
) {
    public ProjectResponse(Long id, String title, String description, ProjectType projectType, LocalDate startDate, LocalDate endDate) {
        this(id, title, description, projectType, null, startDate, endDate);
    }

    public ProjectResponse(Long id, String title, String description, ProjectType projectType, List<StudentResponse> students) {
        this(id, title, description, projectType, students, null, null);
    }
}

