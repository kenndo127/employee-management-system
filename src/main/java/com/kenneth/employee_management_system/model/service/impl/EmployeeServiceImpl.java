package com.kenneth.employee_management_system.model.service.impl;

import com.kenneth.employee_management_system.exceptions.DuplicateEmailException;
import com.kenneth.employee_management_system.exceptions.EmployeeNotFoundException;
import com.kenneth.employee_management_system.exceptions.InvalidEmployeeStateException;
import com.kenneth.employee_management_system.model.entity.Employee;
import com.kenneth.employee_management_system.model.repository.EmployeeRepository;
import com.kenneth.employee_management_system.model.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {

        //Checking duplicate
        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()){
            throw new DuplicateEmailException("Employee is already registered");
        }

        //Checking valid salary range
        if(employee.getDepartment().equalsIgnoreCase("Intern"))
            if(employee.getSalary().compareTo(new BigDecimal("15000")) < 0)
                throw new IllegalArgumentException("Salary must be greater than or equal to 15000 for Interns");

        if(!employee.getDepartment().equalsIgnoreCase("Intern"))
            if(employee.getSalary().compareTo(new BigDecimal("30000")) < 0)
                throw new IllegalArgumentException("Salary must be greater than or equal to 30000 for Employees");


        return employeeRepository.save(employee); //Returns an entity from the database
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll(); // Returns the list of all employees
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found!"));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {

        Employee employeeToUpdate = getEmployeeById(id); // a method defined above

        if(!employeeToUpdate.getEmail().equals(employee.getEmail()))
            if(employeeRepository.findByEmail(employee.getEmail()).isPresent())
                throw new DuplicateEmailException("Duplicate email!! Email: " + employee.getEmail() + " already exists");

        //Checking valid salary range
        if(employee.getDepartment().equalsIgnoreCase("Intern"))
            if(employee.getSalary().compareTo(new BigDecimal("15000")) < 0)
                throw new IllegalArgumentException("Salary must be greater than or equal to 15000 for Interns");

        if(!employee.getDepartment().equalsIgnoreCase("Intern"))
            if(employee.getSalary().compareTo(new BigDecimal("30000")) < 0)
                throw new IllegalArgumentException("Salary must be greater than or equal to 30000 for Employees");

        //Updating
        employeeToUpdate.setDepartment(employee.getDepartment()); //department
        employeeToUpdate.setSalary(employee.getSalary()); //salary
        employeeToUpdate.setActive(employee.isActive()); // active
        employeeToUpdate.setFirstName(employee.getFirstName()); //first name
        employeeToUpdate.setLastName(employee.getLastName()); // last name
        employeeToUpdate.setEmail(employee.getEmail()); //email
        employeeToUpdate.setDateOfJoining(employee.getDateOfJoining()); //date of joining

        //The updatedDate field is not touched here because the @PreUpdate takes care of it

        return employeeRepository.save(employeeToUpdate);
    }

    @Override
    public Employee partialUpdateEmployee(Long id, Employee employee) {
        Employee employeeToUpdate = getEmployeeById(id);

        //For intern salary
        if(employeeToUpdate.getDepartment().equalsIgnoreCase("Intern")){
            if(employee.getSalary().compareTo(new BigDecimal("15000")) < 0)
                throw new IllegalArgumentException("Salary must be greater than or equal to 15000 for Interns");
        }
        else
            employeeToUpdate.setSalary(employee.getSalary());

        //for regular employee salary
        if(!employeeToUpdate.getDepartment().equalsIgnoreCase("Intern")){
            if(employee.getSalary().compareTo(new BigDecimal("30000")) < 0)
                throw new IllegalArgumentException("Salary must be greater than or equal to 30000 for Employees");
        }
        else
            employeeToUpdate.setSalary(employee.getSalary());

        //Other updates
        employeeToUpdate.setDepartment(employee.getDepartment());
        employeeToUpdate.setActive(employee.isActive());

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
        if(employeeToDelete.isActive())
            throw new InvalidEmployeeStateException("Employee is still active");

        employeeRepository.delete(employeeToDelete);
    }

    @Override
    public List<Employee> getEmployeeBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return employeeRepository.findBySalaryRange(minSalary, maxSalary);
    }

}
