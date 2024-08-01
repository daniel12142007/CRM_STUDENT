package com.kaitech.student_crm.exceptions;

public class LevelNotFound extends RuntimeException{
    public LevelNotFound(String message) {
        super(message);
    }
}