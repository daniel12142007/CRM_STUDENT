package com.kaitech.student_crm.exceptions;

public class LevelBadRequest extends RuntimeException {
    public LevelBadRequest(String message) {
        super(message);
    }
}