package com.kaitech.student_crm.dtos;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class DirectionDTO {
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 800)
    private String description;

    private List<StudentDTO> students;


    public DirectionDTO(Long id, String name, List<StudentDTO> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    public DirectionDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty String getName() {
        return name;
    }

    public void setName(@NotEmpty String name) {
        this.name = name;
    }


}
