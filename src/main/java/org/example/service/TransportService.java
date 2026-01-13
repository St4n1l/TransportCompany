package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.TransportDto;
import org.example.dto.TransportUpsertDto;
import org.example.exception.ValidationException;
import org.example.mapper.TransportMapper;
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

    public TransportDto createTransportDto(TransportUpsertDto dto) throws ValidationException {
        if (dto == null || dto.companyId() == null) {
            throw new ValidationException("Company ID is required");
        }
        if (dto.clientId() == null) {
            throw new ValidationException("Client ID is required");
        }

        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ValidationException("Company with ID " + dto.companyId() + " does not exist"));
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ValidationException("Client with ID " + dto.clientId() + " does not exist"));
        Vehicle vehicle = dto.vehicleId() != null ? vehicleRepository.findById(dto.vehicleId()).orElse(null) : null;
        Employee driver = dto.driverId() != null ? employeeRepository.findById(dto.driverId()).orElse(null) : null;

        Transport transport = new Transport();
        TransportMapper.applyUpsert(transport, dto);
        transport.setCompany(company);
        transport.setClient(client);
        transport.setVehicle(vehicle);
        transport.setDriver(driver);

        validate(transport);
        Transport saved = transportRepository.save(transport);
        updateCompanyRevenue(dto.companyId());
        return TransportMapper.toDto(saved);
    }

    public Transport getTransportById(Integer id) {
        return transportRepository.findById(id)
                .orElseThrow(() -> new org.example.exception.NotFoundException("Transport with ID " + id + " not found"));
    }

    public TransportDto getTransportDtoById(Integer id) {
        return TransportMapper.toDto(getTransportById(id));
    }

    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    public List<TransportDto> getAllTransportDtos() {
        return getAllTransports().stream().map(TransportMapper::toDto).toList();
    }

    public List<Transport> getTransportsByDestination(String destination) {
        return transportRepository.findByDestination(destination);
    }

    public List<TransportDto> getTransportDtosByDestination(String destination) {
        return getTransportsByDestination(destination).stream().map(TransportMapper::toDto).toList();
    }

    public List<Transport> getAllTransportsSortedByDestination() {
        return transportRepository.findAllOrderByDestination();
    }

    public List<TransportDto> getAllTransportDtosSortedByDestination() {
        return getAllTransportsSortedByDestination().stream().map(TransportMapper::toDto).toList();
    }

    public List<Transport> getTransportsByCompanyId(Integer companyId) {
        return transportRepository.findByCompanyId(companyId);
    }

    public List<TransportDto> getTransportDtosByCompanyId(Integer companyId) {
        return getTransportsByCompanyId(companyId).stream().map(TransportMapper::toDto).toList();
    }

    public List<Transport> getTransportsByDriverId(Integer driverId) {
        return transportRepository.findByDriverId(driverId);
    }

    public List<TransportDto> getTransportDtosByDriverId(Integer driverId) {
        return getTransportsByDriverId(driverId).stream().map(TransportMapper::toDto).toList();
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

    public TransportDto updateTransportDto(TransportUpsertDto dto) throws ValidationException {
        if (dto == null || dto.id() == null) {
            throw new ValidationException("Transport ID is required for update");
        }
        if (dto.companyId() == null) {
            throw new ValidationException("Company ID is required");
        }
        if (dto.clientId() == null) {
            throw new ValidationException("Client ID is required");
        }

        Transport transport = getTransportById(dto.id());
        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ValidationException("Company with ID " + dto.companyId() + " does not exist"));
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ValidationException("Client with ID " + dto.clientId() + " does not exist"));
        Vehicle vehicle = dto.vehicleId() != null ? vehicleRepository.findById(dto.vehicleId()).orElse(null) : null;
        Employee driver = dto.driverId() != null ? employeeRepository.findById(dto.driverId()).orElse(null) : null;

        TransportMapper.applyUpsert(transport, dto);
        transport.setCompany(company);
        transport.setClient(client);
        transport.setVehicle(vehicle);
        transport.setDriver(driver);

        validate(transport);
        Transport saved = transportRepository.save(transport);
        updateCompanyRevenue(dto.companyId());
        return TransportMapper.toDto(saved);
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

    public TransportDto markAsPaidDto(Integer transportId) {
        return TransportMapper.toDto(markAsPaid(transportId));
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
