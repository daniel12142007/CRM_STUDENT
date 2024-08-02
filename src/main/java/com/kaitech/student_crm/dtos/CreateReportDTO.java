package com.kaitech.student_crm.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
public class CreateReportDTO {
    @NotNull(message = "Activity ID cannot be null")
    private Long activityId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Weeksday ID cannot be null")
    private Long weeksdayId;

    @NotNull(message = "The field should not be empty")
    private boolean isDone;

}
