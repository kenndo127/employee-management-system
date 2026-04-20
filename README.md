# Employee Management System

A RESTful Spring Boot application that manages employee records with full CRUD operations, Excel import/export, and PDF report generation.

## Technology Stack

- Java 21
- Spring Boot 3.5
- Spring Data JPA / Hibernate
- H2 In-Memory Database
- Apache POI 5.x (Excel)
- OpenPDF (PDF)
- Maven

## How to Run

1. Clone the repository
2. Navigate to the project root
3. Run the following command:

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`

## H2 Console

Access the database at `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: (leave blank)

## API Endpoints

### Department Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/v1/departments | Create a department |
| GET | /api/v1/departments | Get all departments |
| GET | /api/v1/departments/{name} | Get department by name |
| PUT | /api/v1/departments/{id} | Update department |
| DELETE | /api/v1/departments/{id} | Delete department |

### Employee Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/v1/employees | Create an employee |
| GET | /api/v1/employees | Get all employees (paginated) |
| GET | /api/v1/employees/{id} | Get employee by ID |
| PUT | /api/v1/employees/{id} | Full update |
| PATCH | /api/v1/employees/{id} | Partial update |
| DELETE | /api/v1/employees/{id} | Soft delete |
| DELETE | /api/v1/employees/{id}/hard | Hard delete |
| GET | /api/v1/employees/salary-range | Filter by salary range |
| POST | /api/v1/employees/import | Import employees from Excel |
| GET | /api/v1/employees/export/excel | Export employees to Excel |
| GET | /api/v1/employees/export/pdf | Export employees to PDF |

## Query Parameters

### GET /api/v1/employees
- `page` - page number (default 0)
- `size` - records per page (default 20)
- `sort` - field to sort by (e.g. firstName)
- `department` - filter by department name
- `active` - filter by active status (true/false)

### GET /api/v1/employees/salary-range
- `min` - minimum salary
- `max` - maximum salary

## Business Rules

- Departments that allow interns have a minimum salary of 15,000
- Departments that do not allow interns have a minimum salary of 30,000
- Deleting an employee sets active = false (soft delete)
- Hard delete only works if the employee is already inactive
- Duplicate emails are rejected

## Excel Import Template

The import file must be `.xlsx` format with these columns:

| Column | Field |
|--------|-------|
| A | firstName |
| B | lastName |
| C | email |
| D | department (must match existing department name) |
| E | salary |
| F | dateOfJoining (YYYY-MM-DD) |
| G | active (TRUE/FALSE) |

## Author

Kenneth Okechukwu Chidiebube
Computer Science Student, University of Ibadan
Faculty of Computing

## License

This project is submitted as an assessment for Naija JUG Bootcamp Cohort 1.
All rights reserved.