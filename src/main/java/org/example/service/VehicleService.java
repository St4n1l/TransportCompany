package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.VehicleDto;
import org.example.dto.VehicleUpsertDto;
import org.example.exception.ValidationException;
import org.example.mapper.VehicleMapper;
import org.example.model.Company;
import org.example.model.Vehicle;
import org.example.repository.CompanyRepository;
import org.example.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CompanyRepository companyRepository;
    private final Validator validator;

    public Vehicle createVehicle(Vehicle vehicle, Integer companyId) throws ValidationException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        vehicle.setCompany(company);
        validate(vehicle);
        return vehicleRepository.save(vehicle);
    }

    public VehicleDto createVehicleDto(VehicleUpsertDto dto) throws ValidationException {
        if (dto == null || dto.companyId() == null) {
            throw new ValidationException("Company ID is required");
        }
        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ValidationException("Company with ID " + dto.companyId() + " does not exist"));
        Vehicle vehicle = new Vehicle();
        VehicleMapper.applyUpsert(vehicle, dto);
        vehicle.setCompany(company);
        validate(vehicle);
        return VehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    public Vehicle getVehicleById(Integer id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new org.example.exception.NotFoundException("Vehicle with ID " + id + " not found"));
    }

    public VehicleDto getVehicleDtoById(Integer id) {
        return VehicleMapper.toDto(getVehicleById(id));
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<VehicleDto> getAllVehicleDtos() {
        return getAllVehicles().stream().map(VehicleMapper::toDto).toList();
    }

    public List<Vehicle> getVehiclesByCompanyId(Integer companyId) {
        return vehicleRepository.findByCompanyId(companyId);
    }

    public List<VehicleDto> getVehicleDtosByCompanyId(Integer companyId) {
        return getVehiclesByCompanyId(companyId).stream().map(VehicleMapper::toDto).toList();
    }

    public Vehicle updateVehicle(Vehicle vehicle, Integer companyId) throws ValidationException {
        if (vehicle.getId() == null) {
            throw new ValidationException("Vehicle ID is required for update");
        }
        if (!vehicleRepository.existsById(vehicle.getId())) {
            throw new ValidationException("Vehicle with ID " + vehicle.getId() + " does not exist");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        vehicle.setCompany(company);
        validate(vehicle);
        return vehicleRepository.save(vehicle);
    }

    public VehicleDto updateVehicleDto(VehicleUpsertDto dto) throws ValidationException {
        if (dto == null || dto.id() == null) {
            throw new ValidationException("Vehicle ID is required for update");
        }
        if (dto.companyId() == null) {
            throw new ValidationException("Company ID is required");
        }
        Vehicle vehicle = getVehicleById(dto.id());
        Company company = companyRepository.findById(dto.companyId())
                .orElseThrow(() -> new ValidationException("Company with ID " + dto.companyId() + " does not exist"));
        VehicleMapper.applyUpsert(vehicle, dto);
        vehicle.setCompany(company);
        validate(vehicle);
        return VehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    public void deleteVehicle(Integer id) {
        vehicleRepository.deleteById(id);
    }

    private void validate(Vehicle vehicle) throws ValidationException {
        Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Vehicle> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(sb.toString());
        }
    }
}
