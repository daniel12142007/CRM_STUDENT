package com.kaitech.student_crm.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Data
public class ActivityDTO {

    private Long id;

    @NotEmpty(message = "Название не должно быть пустым!")
    @Size(min = 2, max = 100, message = "Название должно содержать от 2 до 100 символов!")
    private String title;

    @NotEmpty(message = "Описание не должно быть пустым!")
    @Size(min = 2, max = 100, message = "Описание должно содержать от 2 до 100 символов!")
    private String description;

}
