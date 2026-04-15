package com.kenneth.employee_management_system.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity //makes the objects of the class visible to Spring data JPA
@Getter
@Setter
@NoArgsConstructor//creates a default constructor
@AllArgsConstructor //creates a constructor with all the fields
public class Employee {

    @Id
    @GeneratedValue // This defaults to AUTO
    private long id;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String department;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal salary;

    @NotNull
    @PastOrPresent //future dates are not allowed
    private LocalDate dateOfJoining;

    @NotNull
    private boolean active = true;

    @Column(updatable = false)//prevents update of column after insertion
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() { //run before insertion into db occurs
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() { //run before update in db occurs
        this.updatedAt = LocalDateTime.now();
    }
}
