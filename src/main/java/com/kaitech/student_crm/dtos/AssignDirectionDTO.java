package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.User;

public class AssignDirectionDTO {
    private Long id;
    private User student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
