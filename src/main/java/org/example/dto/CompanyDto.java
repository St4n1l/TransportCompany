package org.example.dto;

import java.math.BigDecimal;

public record CompanyDto(
        Integer id,
        String name,
        String address,
        String phone,
        String email,
        BigDecimal revenue
) {
}

