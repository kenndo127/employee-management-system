package com.kenneth.employee_management_system.model.service.impl;

import com.kenneth.employee_management_system.model.repository.EmployeeRepository;
import com.kenneth.employee_management_system.model.service.PdfExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
public class PdfExportServiceImpl implements PdfExportService {

    private final EmployeeRepository employeeRepository;

    public PdfExportServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void exportEmployees(OutputStream outputStream) throws IOException {

    }
}
