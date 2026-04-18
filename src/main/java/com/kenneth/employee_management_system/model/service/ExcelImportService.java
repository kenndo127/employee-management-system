package com.kenneth.employee_management_system.model.service;

import com.kenneth.employee_management_system.dto.response.ImportResultDto;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelImportService {
    ImportResultDto importEmployees(MultipartFile file);
}
