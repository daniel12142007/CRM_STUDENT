package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTOForAll {
    private Long id;
    private String image;
    private String lastName;
    private String firstName;
    private String direction;
    private Status status;
    private Integer point;

    public StudentDTOForAll(Long id, String image, String lastName, String firstName,
                            String direction, Status status) {
        this.id = id;
        this.image = image;
        this.lastName = lastName;
        this.firstName = firstName;
        this.direction = direction;
        this.status = status;
    }
}
