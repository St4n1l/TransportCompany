package org.example.dto;

public record ClientUpsertDto(
        Integer id,
        Integer companyId,
        String name,
        String contactPerson,
        String phone,
        String email,
        String address
) {
}

