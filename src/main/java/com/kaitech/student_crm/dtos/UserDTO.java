package com.kaitech.student_crm.dtos;

import javax.validation.constraints.NotEmpty;
public class UserDTO {
    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}



