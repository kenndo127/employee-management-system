package com.kenneth.employee_management_system.model.service.impl;

import com.kenneth.employee_management_system.dto.request.EmployeeRequestDto;
import com.kenneth.employee_management_system.exceptions.DepartmentNotFoundException;
import com.kenneth.employee_management_system.exceptions.DuplicateEmailException;
import com.kenneth.employee_management_system.exceptions.EmployeeNotFoundException;
import com.kenneth.employee_management_system.exceptions.InvalidEmployeeStateException;
import com.kenneth.employee_management_system.model.entity.Department;
import com.kenneth.employee_management_system.model.entity.Employee;
import com.kenneth.employee_management_system.model.repository.DepartmentRepository;
import com.kenneth.employee_management_system.model.repository.EmployeeRepository;
import com.kenneth.employee_management_system.model.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Employee createEmployee(EmployeeRequestDto dtoEmployee) {

        //Checking duplicate
        if(employeeRepository.findByEmail(dtoEmployee.getEmail()).isPresent()){
            throw new DuplicateEmailException("Employee is already registered");
        }

        //fetch the department using the department id
        Department department = departmentRepository.findById(dtoEmployee.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist!"));

        //Checking valid salary range
        validateSalary(department, dtoEmployee);

        //Build employee entity
        Employee employeeToBeCreated = new Employee();
        employeeToBeCreated.setFirstName(dtoEmployee.getFirstName());
        employeeToBeCreated.setLastName(dtoEmployee.getLastName());
        employeeToBeCreated.setEmail(dtoEmployee.getEmail());
        employeeToBeCreated.setDepartment(department);
        employeeToBeCreated.setSalary(dtoEmployee.getSalary());
        employeeToBeCreated.setDateOfJoining(dtoEmployee.getDateOfJoining());
        employeeToBeCreated.setActive(dtoEmployee.getActive());

        return employeeRepository.save(employeeToBeCreated); //Returns an entity from the database
    }

    @Override
    public Page<Employee> getAllEmployees(String department, Boolean active, Pageable pageable) {
        return employeeRepository.getAllEmployeesWithFilters(department, active, pageable);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found!"));
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeRequestDto employee) {

        Employee employeeToUpdate = getEmployeeById(id); // a method defined above

        if(!employeeToUpdate.getEmail().equals(employee.getEmail()))
            if(employeeRepository.findByEmail(employee.getEmail()).isPresent())
                throw new DuplicateEmailException("Duplicate email!! Email: " + employee.getEmail() + " already exists");

        //fetch the department using the department id
        Department department = departmentRepository.findById(employee.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist!"));

        //Checking valid salary range
        validateSalary(department, employee);

        //Updating
        employeeToUpdate.setDepartment(department); //department
        employeeToUpdate.setSalary(employee.getSalary()); //salary
        employeeToUpdate.setActive(employee.getActive()); // active
        employeeToUpdate.setFirstName(employee.getFirstName()); //first name
        employeeToUpdate.setLastName(employee.getLastName()); // last name
        employeeToUpdate.setEmail(employee.getEmail()); //email
        employeeToUpdate.setDateOfJoining(employee.getDateOfJoining()); //date of joining

        //The updatedDate field is not touched here because the @PreUpdate takes care of it

        return employeeRepository.save(employeeToUpdate);
    }

    @Override
    public Employee partialUpdateEmployee(Long id, EmployeeRequestDto employee) {
        Employee employeeToUpdate = getEmployeeById(id);

        if(employee.getDepartmentId() != null){
            //fetch the department using the department id
            Department department = departmentRepository.findById(employee.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department does not exist!"));

            employeeToUpdate.setDepartment(department); //update the field
        }

        if(employee.getSalary() != null){
            validateSalary(employeeToUpdate.getDepartment(), employee);
            employeeToUpdate.setSalary(employee.getSalary());
        }

        if(employee.getActive() != null){
            employeeToUpdate.setActive(employee.getActive());
        }

        return employeeRepository.save(employeeToUpdate);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employeeToDelete = getEmployeeById(id);
        employeeToDelete.setActive(false);
        employeeRepository.save(employeeToDelete);
    }

    @Override
    public void hardDeleteEmployee(Long id) {
        Employee employeeToDelete = getEmployeeById(id);

        //hard delete if Employee Active is false
        if(employeeToDelete.getActive())
            throw new InvalidEmployeeStateException("Employee is still active");

        employeeRepository.delete(employeeToDelete);
    }

    @Override
    public List<Employee> getEmployeeBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return employeeRepository.findBySalaryRange(minSalary, maxSalary);
    }

    //Salary validation method
    private void validateSalary(Department department, EmployeeRequestDto employee){
        if(department.getInternAllowed()){
            if(employee.getSalary().compareTo(new BigDecimal("15000")) < 0)
                throw new IllegalArgumentException("Salary must be greater than or equal to 15000 for Interns");
        }else if(employee.getSalary().compareTo(new BigDecimal("30000")) < 0){
            throw new IllegalArgumentException("Salary must be greater than or equal to 30000 for Employees");
        }
    }

}
