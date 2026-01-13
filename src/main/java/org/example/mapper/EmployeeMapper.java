package org.example.mapper;

import org.example.dto.EmployeeDto;
import org.example.dto.EmployeeUpsertDto;
import org.example.model.Employee;

public final class EmployeeMapper {
    private EmployeeMapper() {}

    public static EmployeeDto toDto(Employee employee) {
        if (employee == null) return null;
        Integer companyId = employee.getCompany() != null ? employee.getCompany().getId() : null;
        return new EmployeeDto(
                employee.getId(),
                companyId,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getPosition(),
                employee.getQualification(),
                employee.getSalary(),
                employee.getHireDate()
        );
    }

    public static void applyUpsert(Employee target, EmployeeUpsertDto dto) {
        if (target == null || dto == null) return;
        target.setFirstName(dto.firstName());
        target.setLastName(dto.lastName());
        target.setPhone(dto.phone());
        target.setEmail(dto.email());
        target.setPosition(dto.position());
        target.setQualification(dto.qualification());
        target.setSalary(dto.salary());
        target.setHireDate(dto.hireDate());
    }
}

