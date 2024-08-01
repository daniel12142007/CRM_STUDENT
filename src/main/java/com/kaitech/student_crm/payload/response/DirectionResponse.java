package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.dtos.StudentDTO;

import javax.persistence.Column;
import java.util.List;

public class DirectionResponse {
    private Long id;
    private String name;
    @Column(length = 800)
    private String description;

    private List<StudentDTO> students;

    public DirectionResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public DirectionResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public DirectionResponse(Long id, String name, List<StudentDTO> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    public DirectionResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}
