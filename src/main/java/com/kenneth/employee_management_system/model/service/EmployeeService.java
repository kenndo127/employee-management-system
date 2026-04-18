package com.kenneth.employee_management_system.model.service;

import com.kenneth.employee_management_system.dto.request.EmployeeRequestDto;
import com.kenneth.employee_management_system.model.entity.Employee;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(EmployeeRequestDto employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, EmployeeRequestDto employee);
    Employee partialUpdateEmployee(Long id, EmployeeRequestDto employee);
    void deleteEmployee(Long id);
    void hardDeleteEmployee(Long id);
    List<Employee> getEmployeeBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary);
}
