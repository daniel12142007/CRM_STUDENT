package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.enums.Status;

public class StudentDTOForAll {
    private Long id;
    private String image;
    private String lastName;
    private String firstName;
    private String direction;
    private Status status;

    public StudentDTOForAll(Long id, String image, String lastName, String firstName, String direction, Status status) {
        this.id = id;
        this.image = image;
        this.lastName = lastName;
        this.firstName = firstName;
        this.direction = direction;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
