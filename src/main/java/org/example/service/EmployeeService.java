package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.ValidationException;
import org.example.model.Company;
import org.example.model.Employee;
import org.example.repository.CompanyRepository;
import org.example.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final Validator validator;

    public Employee createEmployee(Employee employee, Integer companyId) throws ValidationException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        employee.setCompany(company);
        validate(employee);
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new org.example.exception.NotFoundException("Employee with ID " + id + " not found"));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByCompanyId(Integer companyId) {
        return employeeRepository.findByCompanyId(companyId);
    }

    public List<Employee> getAllEmployeesSortedByQualification() {
        return employeeRepository.findAllOrderByQualification();
    }

    public List<Employee> getAllEmployeesSortedBySalary() {
        return employeeRepository.findAllOrderBySalaryDesc();
    }

    public List<Employee> getEmployeesByQualification(String qualification) {
        return employeeRepository.findByQualificationContainingIgnoreCase(qualification);
    }

    public Employee updateEmployee(Employee employee, Integer companyId) throws ValidationException {
        if (employee.getId() == null) {
            throw new ValidationException("Employee ID is required for update");
        }
        if (!employeeRepository.existsById(employee.getId())) {
            throw new ValidationException("Employee with ID " + employee.getId() + " does not exist");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        employee.setCompany(company);
        validate(employee);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    private void validate(Employee employee) throws ValidationException {
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Employee> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(sb.toString());
        }
    }
}
