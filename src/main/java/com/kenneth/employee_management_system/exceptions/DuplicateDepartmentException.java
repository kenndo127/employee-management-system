package com.kenneth.employee_management_system.exceptions;

public class DuplicateDepartmentException extends RuntimeException{
    public DuplicateDepartmentException(String message){
        super(message);
    }
}
