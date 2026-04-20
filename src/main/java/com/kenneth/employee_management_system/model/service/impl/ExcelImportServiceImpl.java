package com.kenneth.employee_management_system.model.service.impl;

import com.kenneth.employee_management_system.dto.request.EmployeeRequestDto;
import com.kenneth.employee_management_system.dto.response.ImportResultDto;
import com.kenneth.employee_management_system.exceptions.DepartmentNotFoundException;
import com.kenneth.employee_management_system.exceptions.ExcelProcessingException;
import com.kenneth.employee_management_system.exceptions.InvalidFileFormatException;
import com.kenneth.employee_management_system.model.entity.Department;
import com.kenneth.employee_management_system.model.entity.Employee;
import com.kenneth.employee_management_system.model.repository.DepartmentRepository;
import com.kenneth.employee_management_system.model.repository.EmployeeRepository;
import com.kenneth.employee_management_system.model.service.ExcelImportService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Service
public class ExcelImportServiceImpl implements ExcelImportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final Validator validator;

    public ExcelImportServiceImpl(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            Validator validator
    ){
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public ImportResultDto importEmployees(MultipartFile file) {
        ImportResultDto result = new ImportResultDto();

        // Validate file extension
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".xlsx")) {
            throw new InvalidFileFormatException("Only .xlsx files are accepted");
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);

                if (row == null) continue;

                try {
                    //fetch all cell values as String
                    String firstName = getCellValue(row.getCell(0));
                    String lastName = getCellValue(row.getCell(1));
                    String email = getCellValue(row.getCell(2));
                    BigDecimal salary = new BigDecimal(getCellValue(row.getCell(4)));
                    LocalDate dateOfJoining = LocalDate.parse(getCellValue(row.getCell(5)));
                    boolean active = Boolean.parseBoolean(getCellValue(row.getCell(6)));


                    // Map cells to dto
                    EmployeeRequestDto dto = new EmployeeRequestDto();
                    dto.setFirstName(firstName);
                    dto.setLastName(lastName);
                    dto.setEmail(email);
                    dto.setSalary(salary);
                    dto.setDateOfJoining(dateOfJoining);
                    dto.setActive(active);

                    // Department name from column D
                    String departmentName = getCellValue(row.getCell(3));

                    // Bean validation
                    Set<ConstraintViolation<EmployeeRequestDto>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        int rowNumber = (i + 1);
                        violations.forEach(v ->
                                result.getErrors().add("Row " + rowNumber + ": "
                                        + v.getPropertyPath() + " - " + v.getMessage())
                        );
                        result.setFailureCount(result.getFailureCount() + 1);
                        continue;
                    }

                    // Fetch department by name
                    Department department = departmentRepository.findByName(departmentName)
                            .orElseThrow(() -> new DepartmentNotFoundException(
                                    "Department not found: " + departmentName));

                    // Check duplicate email
                    if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
                        result.getErrors().add("Row " + (i + 1) + ": email - "
                                + dto.getEmail() + " already exists");
                        result.setFailureCount(result.getFailureCount() + 1);
                        continue;
                    }

                    // Validate salary floor
                    BigDecimal floor = department.getInternAllowed()
                            ? new BigDecimal("15000")
                            : new BigDecimal("30000");

                    if (dto.getSalary().compareTo(floor) < 0) {
                        result.getErrors().add("Row " + (i + 1) + ": salary - below minimum of " + floor);
                        result.setFailureCount(result.getFailureCount() + 1);
                        continue;
                    }

                    // Build and save employee
                    Employee employee = new Employee();
                    employee.setFirstName(dto.getFirstName());
                    employee.setLastName(dto.getLastName());
                    employee.setEmail(dto.getEmail());
                    employee.setDepartment(department);
                    employee.setSalary(dto.getSalary());
                    employee.setDateOfJoining(dto.getDateOfJoining());
                    employee.setActive(dto.getActive());

                    employeeRepository.save(employee);
                    result.setSuccessCount(result.getSuccessCount() + 1);

                } catch (DepartmentNotFoundException e) {
                    result.getErrors().add("Row " + (i + 1) + ": " + e.getMessage());
                    result.setFailureCount(result.getFailureCount() + 1);
                } catch (Exception e) {
                    result.getErrors().add("Row " + (i + 1) + ": unexpected error - " + e.getMessage());
                    result.setFailureCount(result.getFailureCount() + 1);
                }
            }

        } catch (InvalidFileFormatException e) {
            throw e;
        } catch (Exception e) {
            throw new ExcelProcessingException("Failed to process Excel file: " + e.getMessage());
        }

        return result;
    }

    private String getCellValue(XSSFCell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                yield String.valueOf((long) cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}