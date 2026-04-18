package com.kenneth.employee_management_system.controllers;

import com.kenneth.employee_management_system.dto.request.DepartmentRequestDto;
import com.kenneth.employee_management_system.dto.response.DepartmentResponseDto;
import com.kenneth.employee_management_system.model.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments") //setting the base api url
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @Valid @RequestBody DepartmentRequestDto department){
        //use requestDto to create a department
        var createdDepartment = departmentService.createDepartment(department.toEntity());

        //use responseDto to return the created department
        DepartmentResponseDto response = DepartmentResponseDto.fromEntity(createdDepartment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentByName(
            @PathVariable("name") String name){
        DepartmentResponseDto response = DepartmentResponseDto.fromEntity(departmentService.getDepartmentByName(name));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable("id") Long id,
            @Valid @RequestBody DepartmentRequestDto department
    ){
        var updatedDepartment = departmentService.updateDept(id, department.toEntity());
        DepartmentResponseDto response = DepartmentResponseDto.fromEntity(updatedDepartment);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments(){
        //Turning all department objects to DepartmentResponseDto Objects
        //since the service returns a list of departments
        List<DepartmentResponseDto> response = departmentService.getAllDepartments()
                                                                .stream()
                                                                .map(department -> DepartmentResponseDto.fromEntity(department))
                                                                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable("id") Long id
    ){
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
