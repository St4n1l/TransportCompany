package org.example.dto;

public record CompanyUpsertDto(
        Integer id,
        String name,
        String address,
        String phone,
        String email
) {
}

