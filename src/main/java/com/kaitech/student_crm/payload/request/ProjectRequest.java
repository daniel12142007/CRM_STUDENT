package com.kaitech.student_crm.payload.request;

import com.kaitech.student_crm.models.enums.ProjectType;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class ProjectRequest {


    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private ProjectType projectType;

    private LocalDate startDate;
    private LocalDate endDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
