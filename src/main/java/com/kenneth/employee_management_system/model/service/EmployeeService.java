package com.kenneth.employee_management_system.model.service;

import com.kenneth.employee_management_system.model.entity.Employee;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employee);
    Employee partialUpdateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
    void hardDeleteEmployee(Long id);
    List<Employee> getEmployeeBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary);
}
