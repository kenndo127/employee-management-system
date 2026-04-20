package com.kenneth.employee_management_system.model.service.impl;

import com.kenneth.employee_management_system.model.repository.EmployeeRepository;
import com.kenneth.employee_management_system.model.service.PdfExportService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
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
        Document document = new Document();

        try { //try-with-resources is not supported by the library
            PdfWriter.getInstance(document, outputStream); //Serves as buffer b/w the output stream and the pdf generator
            document.open();

            Paragraph message = new Paragraph(
                    "PDF export feature is currently under development. Please check back later."
            );
            message.setAlignment(Element.ALIGN_CENTER);
            message.setSpacingBefore(200);
            document.add(message);

        } catch (DocumentException e) {
            throw new IOException("Failed to generate PDF: " + e.getMessage(), e);
        } finally {
            document.close();
        }
    }
}
