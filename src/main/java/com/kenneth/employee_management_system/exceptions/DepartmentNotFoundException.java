package com.kenneth.employee_management_system.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
