package com.kaitech.student_crm.exceptions;

public class ProjectAlreadyCompletedException extends RuntimeException {
    public ProjectAlreadyCompletedException(String message) {
        super(message);
    }
}
