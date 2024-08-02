package com.kaitech.student_crm.models;

import com.kaitech.student_crm.models.enums.ProjectType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProjectType projectType;
    @ManyToMany
    @JoinTable(
            name = "projects_students",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> students = new HashSet<>();

    private LocalDate startDate;
    private LocalDate endDate;


}