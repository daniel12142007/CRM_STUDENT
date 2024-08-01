package com.kaitech.student_crm.exceptions;


public class ActivityNotCreatedException extends RuntimeException{
    public ActivityNotCreatedException(String msg){
        super(msg);
    }
    public ActivityNotCreatedException(){}

}
