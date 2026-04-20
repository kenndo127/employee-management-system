package com.kenneth.employee_management_system.model.service;

import java.io.IOException;
import java.io.OutputStream;

public interface PdfExportService {
    void exportEmployees(OutputStream outputStream) throws IOException;
}
