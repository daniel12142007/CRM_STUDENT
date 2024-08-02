package com.kaitech.student_crm.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
public class DirectionDTO {
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 800)
    private String description;

    private List<StudentDTO> students;





}
