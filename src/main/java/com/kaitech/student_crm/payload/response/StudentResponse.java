package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.models.enums.Status;

import java.util.List;

public record StudentResponse(Long id,
                              String image,
                              String firstName,
                              String lastName,
                              String email,
                              String phoneNumber,
                              String direction,
                              List<ProjectResponse> projects,
                              Status status,
                              LevelResponse level) {

    public StudentResponse(Long id, String image, String firstName, String lastName, String email) {
        this(id, image, firstName, lastName, email, null, null, null, null, null);
    }

    public StudentResponse(Long id, String image, String firstName, String lastName, String email, String phoneNumber, String direction, Status status) {
        this(id, image, firstName, lastName, email, phoneNumber, direction, null, status, null);
    }
    public StudentResponse(Long id, String image, String firstName, String lastName, String email,
                           String phoneNumber, String direction, Status status,LevelResponse level) {
        this(id, image, firstName, lastName, email, phoneNumber, direction, null, status, level);
    }
    public StudentResponse(Long id, String image, String firstName, String lastName, String email,
                           String phoneNumber, Status status,LevelResponse level) {
        this(id, image, firstName, lastName, email, phoneNumber, null, null, status, level);
    }
    public StudentResponse(Long id, String image, String firstName, String lastName, String email,
                           String phoneNumber) {
        this(id, image, firstName, lastName, email, phoneNumber, null, null, null, null);
    }
}
