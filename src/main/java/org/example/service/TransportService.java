package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.ValidationException;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class TransportService {
    private final TransportRepository transportRepository;
    private final CompanyRepository companyRepository;
    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;
    private final EmployeeRepository employeeRepository;
    private final Validator validator;

    public Transport createTransport(Transport transport, Integer companyId, Integer clientId, 
                                    Integer vehicleId, Integer driverId) throws ValidationException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ValidationException("Client with ID " + clientId + " does not exist"));
        Vehicle vehicle = vehicleId != null ? vehicleRepository.findById(vehicleId).orElse(null) : null;
        Employee driver = driverId != null ? employeeRepository.findById(driverId).orElse(null) : null;

        transport.setCompany(company);
        transport.setClient(client);
        transport.setVehicle(vehicle);
        transport.setDriver(driver);

        validate(transport);
        Transport saved = transportRepository.save(transport);
        updateCompanyRevenue(companyId);
        return saved;
    }

    public Transport getTransportById(Integer id) {
        return transportRepository.findById(id)
                .orElseThrow(() -> new org.example.exception.NotFoundException("Transport with ID " + id + " not found"));
    }

    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    public List<Transport> getTransportsByDestination(String destination) {
        return transportRepository.findByDestination(destination);
    }

    public List<Transport> getAllTransportsSortedByDestination() {
        return transportRepository.findAllOrderByDestination();
    }

    public List<Transport> getTransportsByCompanyId(Integer companyId) {
        return transportRepository.findByCompanyId(companyId);
    }

    public List<Transport> getTransportsByDriverId(Integer driverId) {
        return transportRepository.findByDriverId(driverId);
    }

    public List<Transport> getTransportsByDateRange(Integer companyId, LocalDateTime startDate, LocalDateTime endDate) {
        return transportRepository.findByCompanyIdAndDateRange(companyId, startDate, endDate);
    }

    public Transport updateTransport(Transport transport, Integer companyId, Integer clientId,
                                    Integer vehicleId, Integer driverId) throws ValidationException {
        if (transport.getId() == null) {
            throw new ValidationException("Transport ID is required for update");
        }
        if (!transportRepository.existsById(transport.getId())) {
            throw new ValidationException("Transport with ID " + transport.getId() + " does not exist");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ValidationException("Client with ID " + clientId + " does not exist"));
        Vehicle vehicle = vehicleId != null ? vehicleRepository.findById(vehicleId).orElse(null) : null;
        Employee driver = driverId != null ? employeeRepository.findById(driverId).orElse(null) : null;

        transport.setCompany(company);
        transport.setClient(client);
        transport.setVehicle(vehicle);
        transport.setDriver(driver);

        validate(transport);
        Transport saved = transportRepository.save(transport);
        updateCompanyRevenue(companyId);
        return saved;
    }

    public void deleteTransport(Integer id) {
        Transport transport = transportRepository.findById(id).orElse(null);
        if (transport != null) {
            Integer companyId = transport.getCompany().getId();
            transportRepository.deleteById(id);
            updateCompanyRevenue(companyId);
        }
    }

    public Transport markAsPaid(Integer transportId) {
        Transport transport = getTransportById(transportId);
        transport.setIsPaid(true);
        return transportRepository.save(transport);
    }

    private void updateCompanyRevenue(Integer companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company != null) {
            List<Transport> transports = transportRepository.findByCompanyId(companyId);
            BigDecimal totalRevenue = transports.stream()
                    .map(Transport::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            company.setRevenue(totalRevenue);
            companyRepository.save(company);
        }
    }

    private void validate(Transport transport) throws ValidationException {
        Set<ConstraintViolation<Transport>> violations = validator.validate(transport);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Transport> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(sb.toString());
        }
        
        // Additional business logic validation
        if ("CARGO".equals(transport.getTransportType()) && transport.getCargoWeight() == null) {
            throw new ValidationException("Cargo weight is required for cargo transports");
        }
        if ("PASSENGER".equals(transport.getTransportType()) && transport.getPassengerCount() == null) {
            throw new ValidationException("Passenger count is required for passenger transports");
        }
    }
}
