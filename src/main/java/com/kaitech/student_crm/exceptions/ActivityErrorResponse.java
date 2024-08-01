package com.kaitech.student_crm.exceptions;

public class ActivityErrorResponse {
    private String message;
    private Long time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public ActivityErrorResponse(String message, Long time) {
        this.message = message;
        this.time = time;
    }
}
