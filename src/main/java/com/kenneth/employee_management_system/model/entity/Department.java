package com.kenneth.employee_management_system.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String name;

    @NotNull
    private Boolean internAllowed;
}
