package org.example.mapper;

import org.example.dto.VehicleDto;
import org.example.dto.VehicleUpsertDto;
import org.example.model.Vehicle;

public final class VehicleMapper {
    private VehicleMapper() {}

    public static VehicleDto toDto(Vehicle vehicle) {
        if (vehicle == null) return null;
        Integer companyId = vehicle.getCompany() != null ? vehicle.getCompany().getId() : null;
        return new VehicleDto(
                vehicle.getId(),
                companyId,
                vehicle.getLicensePlate(),
                vehicle.getVehicleType(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getCapacity()
        );
    }

    public static void applyUpsert(Vehicle target, VehicleUpsertDto dto) {
        if (target == null || dto == null) return;
        target.setLicensePlate(dto.licensePlate());
        target.setVehicleType(dto.vehicleType());
        target.setBrand(dto.brand());
        target.setModel(dto.model());
        target.setYear(dto.year());
        target.setCapacity(dto.capacity());
    }
}

