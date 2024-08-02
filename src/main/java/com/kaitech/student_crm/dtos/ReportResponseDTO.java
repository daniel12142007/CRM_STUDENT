package com.kaitech.student_crm.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ReportResponseDTO {
    private Long id;
    private StudentDTO student;
    private ActivityDTO activity;
    private WeeksdayDTO weeksday;
    private boolean isDone;

}
