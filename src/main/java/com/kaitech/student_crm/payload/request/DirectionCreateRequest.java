package com.kaitech.student_crm.payload.request;


import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
@Data

public class DirectionCreateRequest {
    @NotEmpty
    private String name;
    @Column(length = 800)
    private String description;
}
