package com.kenneth.employee_management_system.dto.request;

import com.kenneth.employee_management_system.model.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDto {
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    private Boolean internAllowed;

    public Department toEntity() {
        Department department = new Department();

        department.setName(this.name);
        department.setInternAllowed(this.internAllowed);

        return department;
    }
}
