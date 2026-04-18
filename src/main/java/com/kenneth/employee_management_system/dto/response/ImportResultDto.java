package com.kenneth.employee_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportResultDto {
    private int successCount;
    private int failureCount;

    List<String> errors = new ArrayList<>();
}
