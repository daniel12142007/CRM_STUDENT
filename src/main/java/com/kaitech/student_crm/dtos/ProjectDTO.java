package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.Student;
import com.kaitech.student_crm.models.enums.ProjectType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
@NoArgsConstructor
public class ProjectDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;

    @NotEmpty
    private ProjectType projectType;

    private List<Student> students;

}
