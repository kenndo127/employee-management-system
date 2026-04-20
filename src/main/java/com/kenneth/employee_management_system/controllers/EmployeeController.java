package com.kenneth.employee_management_system.controllers;

import com.kenneth.employee_management_system.dto.request.EmployeeRequestDto;
import com.kenneth.employee_management_system.dto.response.EmployeeResponseDto;
import com.kenneth.employee_management_system.dto.response.ImportResultDto;
import com.kenneth.employee_management_system.model.entity.Employee;
import com.kenneth.employee_management_system.model.service.EmployeeService;
import com.kenneth.employee_management_system.model.service.ExcelExportService;
import com.kenneth.employee_management_system.model.service.ExcelImportService;
import com.kenneth.employee_management_system.model.service.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
// the legal aspect of data acquisition in
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ExcelImportService excelImportService;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;

    public EmployeeController(
            EmployeeService employeeService,
            ExcelImportService excelImportService,
            ExcelExportService excelExportService,
            PdfExportService pdfExportService
    ){
        this.employeeService = employeeService;
        this.excelImportService = excelImportService;
        this.excelExportService = excelExportService;
        this.pdfExportService = pdfExportService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @Valid @RequestBody EmployeeRequestDto employee
    ){
        EmployeeResponseDto response = EmployeeResponseDto.fromEntity(employeeService.createEmployee(employee));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> getAllEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ){
        Page<EmployeeResponseDto> response = employeeService.getAllEmployees(department, active, pageable)
                .map(Employee -> EmployeeResponseDto.fromEntity(Employee));

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

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees_" +
                System.currentTimeMillis() + ".xlsx");
        excelExportService.exportEmployees(response.getOutputStream());
    }

    @GetMapping("/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=employee_report_" +
                System.currentTimeMillis() + ".pdf");
        pdfExportService.exportEmployees(response.getOutputStream());
    }
}
