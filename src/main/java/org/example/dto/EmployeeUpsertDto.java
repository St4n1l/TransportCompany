package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeUpsertDto(
        Integer id,
        Integer companyId,
        String firstName,
        String lastName,
        String phone,
        String email,
        String position,
        String qualification,
        BigDecimal salary,
        LocalDate hireDate
) {
}

