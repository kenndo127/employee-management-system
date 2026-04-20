package com.kenneth.employee_management_system.model.repository;

import com.kenneth.employee_management_system.model.entity.Department;
import com.kenneth.employee_management_system.model.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment(Department department);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByActiveTrue();

    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:department IS NULL OR e.department.name = :department) AND " +
            "(:active IS NULL OR e.active = :active)")
    Page<Employee> getAllEmployeesWithFilters(@Param("department") String department,
                                   @Param("active") Boolean active,
                                   Pageable pageable);

    List<Employee> id(long id);
}
