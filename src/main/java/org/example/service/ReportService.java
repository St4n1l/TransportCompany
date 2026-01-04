package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Company;
import org.example.model.Employee;
import org.example.model.Transport;
import org.example.repository.CompanyRepository;
import org.example.repository.EmployeeRepository;
import org.example.repository.TransportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final TransportRepository transportRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    public int getTotalTransportCount() {
        return (int) transportRepository.count();
    }

    public int getTotalTransportCountByCompany(Integer companyId) {
        return transportRepository.findByCompanyId(companyId).size();
    }

    public BigDecimal getTotalRevenue() {
        List<Transport> transports = transportRepository.findAll();
        return transports.stream()
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalRevenueByCompany(Integer companyId) {
        List<Transport> transports = transportRepository.findByCompanyId(companyId);
        return transports.stream()
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Employee, Integer> getDriverTransportCounts(Integer companyId) {
        Map<Employee, Integer> driverCounts = new HashMap<>();
        List<Employee> employees = employeeRepository.findByCompanyId(companyId);
        
        for (Employee employee : employees) {
            if ("DRIVER".equalsIgnoreCase(employee.getPosition())) {
                List<Transport> transports = transportRepository.findByDriverId(employee.getId());
                driverCounts.put(employee, transports.size());
            }
        }
        
        return driverCounts;
    }

    public Map<Employee, BigDecimal> getDriverRevenues(Integer companyId) {
        Map<Employee, BigDecimal> driverRevenues = new HashMap<>();
        List<Employee> employees = employeeRepository.findByCompanyId(companyId);
        
        for (Employee employee : employees) {
            if ("DRIVER".equalsIgnoreCase(employee.getPosition())) {
                List<Transport> transports = transportRepository.findByDriverId(employee.getId());
                BigDecimal revenue = transports.stream()
                        .map(Transport::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                driverRevenues.put(employee, revenue);
            }
        }
        
        return driverRevenues;
    }

    public BigDecimal getCompanyRevenueForPeriod(Integer companyId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transport> transports = transportRepository.findByCompanyIdAndDateRange(companyId, startDate, endDate);
        return transports.stream()
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void printTotalTransportCount() {
        int count = getTotalTransportCount();
        System.out.println("Total number of transports: " + count);
    }

    public void printTotalRevenue() {
        BigDecimal revenue = getTotalRevenue();
        System.out.println("Total revenue from all transports: " + revenue);
    }

    public void printDriverTransportCounts(Integer companyId) {
        Map<Employee, Integer> driverCounts = getDriverTransportCounts(companyId);
        System.out.println("\nDriver Transport Counts:");
        System.out.println("------------------------");
        if (driverCounts.isEmpty()) {
            System.out.println("No drivers found for this company.");
        } else {
            for (Map.Entry<Employee, Integer> entry : driverCounts.entrySet()) {
                Employee driver = entry.getKey();
                Integer count = entry.getValue();
                System.out.println(driver.getFullName() + " (ID: " + driver.getId() + "): " + count + " transports");
            }
        }
    }

    public void printDriverRevenues(Integer companyId) {
        Map<Employee, BigDecimal> driverRevenues = getDriverRevenues(companyId);
        System.out.println("\nDriver Revenues:");
        System.out.println("----------------");
        if (driverRevenues.isEmpty()) {
            System.out.println("No drivers found for this company.");
        } else {
            for (Map.Entry<Employee, BigDecimal> entry : driverRevenues.entrySet()) {
                Employee driver = entry.getKey();
                BigDecimal revenue = entry.getValue();
                System.out.println(driver.getFullName() + " (ID: " + driver.getId() + "): " + revenue);
            }
        }
    }

    public void printCompanyRevenueForPeriod(Integer companyId, LocalDateTime startDate, LocalDateTime endDate) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            System.out.println("Company not found.");
            return;
        }
        
        BigDecimal revenue = getCompanyRevenueForPeriod(companyId, startDate, endDate);
        System.out.println("\nCompany Revenue Report:");
        System.out.println("----------------------");
        System.out.println("Company: " + company.getName());
        System.out.println("Period: " + startDate.toLocalDate() + " to " + endDate.toLocalDate());
        System.out.println("Revenue: " + revenue);
    }
}
