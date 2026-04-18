package com.kenneth.employee_management_system.dto.response;

import com.kenneth.employee_management_system.model.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponseDto {
    private Long id;

    private String name;

    private Boolean internAllowed;

    public static DepartmentResponseDto fromEntity(Department department) {
        DepartmentResponseDto departmentResponseDto = new DepartmentResponseDto();
        departmentResponseDto.setId(department.getId());
        departmentResponseDto.setName(department.getName());
        departmentResponseDto.setInternAllowed(department.getInternAllowed());
        return departmentResponseDto;
    }
}
