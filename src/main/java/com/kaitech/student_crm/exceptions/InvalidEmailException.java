package com.kaitech.student_crm.exceptions;


public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }
}