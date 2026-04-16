package com.kenneth.employee_management_system.exceptions;

public class InvalidEmployeeStateException extends RuntimeException {
    public InvalidEmployeeStateException(String message) {
        super(message);
    }
}
