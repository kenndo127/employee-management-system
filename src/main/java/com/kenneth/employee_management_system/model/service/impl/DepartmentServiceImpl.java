package com.kenneth.employee_management_system.model.service.impl;

import com.kenneth.employee_management_system.exceptions.DepartmentNotFoundException;
import com.kenneth.employee_management_system.exceptions.DuplicateDepartmentException;
import com.kenneth.employee_management_system.model.entity.Department;
import com.kenneth.employee_management_system.model.repository.DepartmentRepository;
import com.kenneth.employee_management_system.model.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        if(departmentRepository.findByName(department.getName()).isPresent())
            throw new DuplicateDepartmentException("Department already exist!");

        return departmentRepository.save(department);
    }

    @Override
    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist!"));
    }

    @Override
    public Department updateDept(Long id, Department department) {
        //fetch department
        Department departmentToUpdate = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist"));

        //prevent duplicate department
        if(!departmentToUpdate.getName().equals(department.getName())){
            if(departmentRepository.findByName(department.getName()).isPresent())
                throw new DuplicateDepartmentException("Can't have two departments with the same names");
        }

        // Update department
        departmentToUpdate.setName(department.getName());
        departmentToUpdate.setInternAllowed(department.getInternAllowed());

        return departmentRepository.save(departmentToUpdate);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public void deleteDepartment(Long id){
        Department deptToBeDeleted = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist!"));
        departmentRepository.delete(deptToBeDeleted);
    }
}
