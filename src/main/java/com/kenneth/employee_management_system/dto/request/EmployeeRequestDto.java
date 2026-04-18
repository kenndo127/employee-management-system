package com.kenneth.employee_management_system.dto.request;

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

    @NotNull
    private Long departmentId;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal salary;

    @NotNull
    @PastOrPresent //future dates are not allowed
    private LocalDate dateOfJoining;

    @NotNull
    private Boolean active = true;
}
