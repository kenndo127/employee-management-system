package com.kenneth.employee_management_system.model.service;

import com.kenneth.employee_management_system.model.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);
    Department updateDept(Long id, Department department);
    Department getDepartmentByName(String name);
    List<Department> getAllDepartments();
    void deleteDepartment(Long id);
}
