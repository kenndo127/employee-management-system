package com.kenneth.employee_management_system.dto;

import com.kenneth.employee_management_system.model.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String department;

    private BigDecimal salary;

    private LocalDate dateOfJoining;

    private Boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static EmployeeResponseDto fromEntity(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setSalary(employee.getSalary());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setActive(employee.getActive());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());
        return dto;
    }
}
