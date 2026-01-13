package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CompanyDto;
import org.example.dto.CompanyUpsertDto;
import org.example.exception.ValidationException;
import org.example.mapper.CompanyMapper;
import org.example.model.Company;
import org.example.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final Validator validator;

    public Company createCompany(Company company) throws ValidationException {
        validate(company);
        return companyRepository.save(company);
    }

    public CompanyDto createCompanyDto(CompanyUpsertDto dto) throws ValidationException {
        Company company = new Company();
        CompanyMapper.applyUpsert(company, dto);
        validate(company);
        return CompanyMapper.toDto(companyRepository.save(company));
    }

    public Company getCompanyById(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new org.example.exception.NotFoundException("Company with ID " + id + " not found"));
    }

    public CompanyDto getCompanyDtoById(Integer id) {
        return CompanyMapper.toDto(getCompanyById(id));
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public List<CompanyDto> getAllCompanyDtos() {
        return getAllCompanies().stream().map(CompanyMapper::toDto).toList();
    }

    public List<Company> getAllCompaniesSortedByName() {
        return companyRepository.findAllByOrderByName();
    }

    public List<CompanyDto> getAllCompanyDtosSortedByName() {
        return getAllCompaniesSortedByName().stream().map(CompanyMapper::toDto).toList();
    }

    public List<Company> getAllCompaniesSortedByRevenue() {
        return companyRepository.findAllByOrderByRevenueDesc();
    }

    public List<CompanyDto> getAllCompanyDtosSortedByRevenue() {
        return getAllCompaniesSortedByRevenue().stream().map(CompanyMapper::toDto).toList();
    }

    public Company updateCompany(Company company) throws ValidationException {
        if (company.getId() == null) {
            throw new ValidationException("Company ID is required for update");
        }
        if (!companyRepository.existsById(company.getId())) {
            throw new ValidationException("Company with ID " + company.getId() + " does not exist");
        }
        validate(company);
        return companyRepository.save(company);
    }

    public CompanyDto updateCompanyDto(CompanyUpsertDto dto) throws ValidationException {
        if (dto == null || dto.id() == null) {
            throw new ValidationException("Company ID is required for update");
        }
        Company company = getCompanyById(dto.id());
        CompanyMapper.applyUpsert(company, dto);
        validate(company);
        return CompanyMapper.toDto(companyRepository.save(company));
    }

    public void deleteCompany(Integer id) {
        companyRepository.deleteById(id);
    }

    private void validate(Company company) throws ValidationException {
        Set<ConstraintViolation<Company>> violations = validator.validate(company);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Company> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(sb.toString());
        }
    }
}
