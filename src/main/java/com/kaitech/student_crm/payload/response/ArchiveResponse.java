package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArchiveResponse {
    private Long id;
    private LocalDate dateUpdate;
    private String newLevel;
    private String oldLevel;
    private Boolean status;
    private Student student;
}
