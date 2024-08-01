package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.enums.ProjectType;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ProjectDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;

    @NotEmpty
    private ProjectType projectType;

    private List<Student> students;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
