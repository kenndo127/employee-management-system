package com.kenneth.employee_management_system.model.service.impl;

import com.kenneth.employee_management_system.model.entity.Employee;
import com.kenneth.employee_management_system.model.repository.EmployeeRepository;
import com.kenneth.employee_management_system.model.service.ExcelExportService;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    private final EmployeeRepository employeeRepository;

    public ExcelExportServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void exportEmployees(OutputStream outputStream) throws IOException{

        //Used to fetch all Employee entities in the Database
        List<Employee> employees = employeeRepository.findAll();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Employees");

            // Header style
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);

            // Alternating row styles
            XSSFCellStyle whiteStyle = workbook.createCellStyle();
            whiteStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            whiteStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFCellStyle blueStyle = workbook.createCellStyle();
            blueStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            blueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Salary style
            XSSFCellStyle salaryStyle = workbook.createCellStyle();
            salaryStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            // Header row
            String[] headers = {"id", "firstName", "lastName", "email", "department",
                    "salary", "dateOfJoining", "active", "createdAt", "updatedAt"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 1;
            for (Employee emp : employees) {
                XSSFRow row = sheet.createRow(rowNum);
                XSSFCellStyle rowStyle = rowNum % 2 == 0 ? blueStyle : whiteStyle;

                row.createCell(0).setCellValue(emp.getId());
                row.createCell(1).setCellValue(emp.getFirstName());
                row.createCell(2).setCellValue(emp.getLastName());
                row.createCell(3).setCellValue(emp.getEmail());
                row.createCell(4).setCellValue(emp.getDepartment().getName());

                XSSFCell salaryCell = row.createCell(5);
                salaryCell.setCellValue(emp.getSalary().doubleValue());
                salaryCell.setCellStyle(salaryStyle);

                row.createCell(6).setCellValue(emp.getDateOfJoining().toString());
                row.createCell(7).setCellValue(emp.getActive());
                row.createCell(8).setCellValue(emp.getCreatedAt() != null ? emp.getCreatedAt().toString() : "");
                row.createCell(9).setCellValue(emp.getUpdatedAt() != null ? emp.getUpdatedAt().toString() : "");

                for (int i = 0; i < headers.length; i++) {
                    if (i != 5) row.getCell(i).setCellStyle(rowStyle);
                }

                rowNum++;
            }

            // Auto size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        }
    }
}
