package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
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


}
