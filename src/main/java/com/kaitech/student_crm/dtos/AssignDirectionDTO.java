package com.kaitech.student_crm.dtos;

import com.kaitech.student_crm.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AssignDirectionDTO {
    private Long id;
    private User student;

}
