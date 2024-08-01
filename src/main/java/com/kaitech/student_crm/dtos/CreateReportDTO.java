package com.kaitech.student_crm.dtos;

import javax.validation.constraints.NotNull;

public class CreateReportDTO {
    @NotNull(message = "Activity ID cannot be null")
    private Long activityId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Weeksday ID cannot be null")
    private Long weeksdayId;

    @NotNull(message = "The field should not be empty")
    private boolean isDone;


    public @NotNull(message = "Activity ID cannot be null") Long getActivityId() {
        return activityId;
    }

    public void setActivityId(@NotNull(message = "Activity ID cannot be null") Long activityId) {
        this.activityId = activityId;
    }

    public @NotNull(message = "User ID cannot be null") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User ID cannot be null") Long userId) {
        this.userId = userId;
    }

    public @NotNull(message = "Weeksday ID cannot be null") Long getWeeksdayId() {
        return weeksdayId;
    }

    public void setWeeksdayId(@NotNull(message = "Weeksday ID cannot be null") Long weeksdayId) {
        this.weeksdayId = weeksdayId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
