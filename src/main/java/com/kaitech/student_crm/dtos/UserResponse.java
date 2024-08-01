package com.kaitech.student_crm.dtos;

public record UserResponse(Long id,
                           String firstName,
                           String lastName,
                           String email) {

}