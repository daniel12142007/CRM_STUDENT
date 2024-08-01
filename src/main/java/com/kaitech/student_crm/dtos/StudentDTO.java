package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.enums.Status;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class StudentDTO {
    private Long id;
    private String image;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    private String email;
    @NotEmpty
    private String phoneNumber;
    private String direction;
    private List<String> projects;
    private Status status;
    private String level;
    private Integer point;

    public StudentDTO() {
    }


    public StudentDTO(Long id, String image, String firstname,
                      String lastname, String email, String phoneNumber,
                      String direction, Status status,
                      String level, Integer point) {
        this.id = id;
        this.image = image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.direction = direction;
        this.status = status;
        this.level = level;
        this.point = point;
    }

    public StudentDTO(Long id, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public StudentDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotEmpty String firstname) {
        this.firstname = firstname;
    }

    public @NotEmpty String getLastname() {
        return lastname;
    }

    public void setLastname(@NotEmpty String lastname) {
        this.lastname = lastname;
    }

    public @NotEmpty String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty String email) {
        this.email = email;
    }

    public @NotEmpty String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotEmpty String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
