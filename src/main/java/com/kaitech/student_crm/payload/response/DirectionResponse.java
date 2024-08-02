package com.kaitech.student_crm.payload.response;

import com.kaitech.student_crm.dtos.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectionResponse {
    private Long id;
    private String name;
    @Column(length = 800)
    private String description;

    private List<StudentDTO> students;

    public DirectionResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public DirectionResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
