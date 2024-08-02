package com.kaitech.student_crm.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
@Data
public class WeeksdayDTO {

    private Long id;
    @NotEmpty
    private String name;

}
