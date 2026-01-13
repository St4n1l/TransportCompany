package org.example.dto;

public record ClientDto(
        Integer id,
        Integer companyId,
        String name,
        String contactPerson,
        String phone,
        String email,
        String address
) {
}

