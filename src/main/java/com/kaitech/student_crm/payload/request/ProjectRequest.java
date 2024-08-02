package com.kaitech.student_crm.payload.request;

import com.kaitech.student_crm.models.enums.ProjectType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
@Data
public class ProjectRequest {


    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private ProjectType projectType;

    private LocalDate startDate;
    private LocalDate endDate;

}