package org.example.mapper;

import org.example.dto.CompanyDto;
import org.example.dto.CompanyUpsertDto;
import org.example.model.Company;

public final class CompanyMapper {
    private CompanyMapper() {}

    public static CompanyDto toDto(Company company) {
        if (company == null) return null;
        return new CompanyDto(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getPhone(),
                company.getEmail(),
                company.getRevenue()
        );
    }

    public static void applyUpsert(Company target, CompanyUpsertDto dto) {
        if (target == null || dto == null) return;
        target.setName(dto.name());
        target.setAddress(dto.address());
        target.setPhone(dto.phone());
        target.setEmail(dto.email());
        // NOTE: revenue is computed from transports; not set from DTO.
    }
}

