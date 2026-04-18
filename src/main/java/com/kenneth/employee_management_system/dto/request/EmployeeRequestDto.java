package com.kenneth.employee_management_system.dto.request;

import com.kenneth.employee_management_system.model.entity.Department;
import com.kenneth.employee_management_system.model.entity.Employee;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private Department department;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal salary;

    @NotNull
    @PastOrPresent //future dates are not allowed
    private LocalDate dateOfJoining;

    @NotNull
    private Boolean active = true;

    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setFirstName(this.firstName);
        employee.setLastName(this.lastName);
        employee.setEmail(this.email);
        employee.setDepartment(this.department);
        employee.setSalary(this.salary);
        employee.setDateOfJoining(this.dateOfJoining);
        employee.setActive(this.active);
        return employee;
    }
}
