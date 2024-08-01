package com.kaitech.student_crm.payload.request;


import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

public class DirectionCreateRequest {
    @NotEmpty
    private String name;
    @Column(length = 800)
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
