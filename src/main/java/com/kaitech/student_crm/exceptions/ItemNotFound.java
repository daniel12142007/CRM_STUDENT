package com.kaitech.student_crm.exceptions;

public class ItemNotFound extends RuntimeException{
    public ItemNotFound(String message) {
        super(message);
    }
}