package com.kaitech.student_crm.exceptions;

public class ActivityNotFoundException extends RuntimeException{
    public ActivityNotFoundException(String msg){
        super(msg);
    }
}
