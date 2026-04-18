package com.kenneth.employee_management_system.controllers;

import com.kenneth.employee_management_system.dto.request.EmployeeRequestDto;
import com.kenneth.employee_management_system.dto.response.EmployeeResponseDto;
import com.kenneth.employee_management_system.dto.response.ImportResultDto;
import com.kenneth.employee_management_system.model.service.EmployeeService;
import com.kenneth.employee_management_system.model.service.ExcelImportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
// the legal aspect of data acquisition in
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ExcelImportService excelImportService;

    public EmployeeController(
            EmployeeService employeeService,
            ExcelImportService excelImportService
    ){
        this.employeeService = employeeService;
        this.excelImportService = excelImportService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @Valid @RequestBody EmployeeRequestDto employee
    ){
        EmployeeResponseDto response = EmployeeResponseDto.fromEntity(employeeService.createEmployee(employee));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees(){
        List<EmployeeResponseDto> response = employeeService.getAllEmployees()
                .stream().map(Employee -> EmployeeResponseDto.fromEntity(Employee)).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(
            @PathVariable("id") Long id
    ){
        EmployeeResponseDto response = EmployeeResponseDto.fromEntity(employeeService.getEmployeeById(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable("id") Long id,
            @Valid @RequestBody EmployeeRequestDto employee
    ){
        EmployeeResponseDto response = EmployeeResponseDto.fromEntity(employeeService.updateEmployee(id, employee));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> partialUpdateEmployee(
            @PathVariable("id") Long id,
            @RequestBody EmployeeRequestDto employee
    ){
        EmployeeResponseDto response = EmployeeResponseDto.fromEntity(employeeService.partialUpdateEmployee(id, employee));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(
            @PathVariable("id") Long id
    ){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(
            @PathVariable("id") Long id
    ){
        employeeService.hardDeleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<EmployeeResponseDto>> getEmployeeBySalaryRange(
            @RequestParam BigDecimal min, @RequestParam BigDecimal max
    ){
        List<EmployeeResponseDto> response = employeeService.getEmployeeBySalaryRange(min, max)
                .stream().map(employee -> EmployeeResponseDto.fromEntity(employee)).toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResultDto> importEmployees(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(excelImportService.importEmployees(file));
    }
}
