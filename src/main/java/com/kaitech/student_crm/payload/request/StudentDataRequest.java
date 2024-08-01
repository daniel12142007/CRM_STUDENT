package com.kaitech.student_crm.payload.request;

import com.kaitech.student_crm.annotations.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class StudentDataRequest {
    @NotEmpty(message = "Please enter student's image")
    private String image;
    @Email(message = "It should have email format")
    @NotBlank(message = "Student email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Please enter student's name")
    private String firstname;

    @NotEmpty(message = "Please enter student's lastname")
    private String lastname;

    @NotEmpty(message = "Please enter student's phone number")
    private String phoneNumber;

    public @Email(message = "It should have email format") @NotBlank(message = "Student email is required") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "It should have email format") @NotBlank(message = "Student email is required") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Please enter student's name") String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotEmpty(message = "Please enter student's name") String firstname) {
        this.firstname = firstname;
    }

    public @NotEmpty(message = "Please enter student's lastname") String getLastname() {
        return lastname;
    }

    public void setLastname(@NotEmpty(message = "Please enter student's lastname") String lastname) {
        this.lastname = lastname;
    }

    public @NotEmpty(message = "Please enter student's phone number") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotEmpty(message = "Please enter student's phone number") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}