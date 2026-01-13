package org.example.dto;

import java.math.BigDecimal;

public record VehicleUpsertDto(
        Integer id,
        Integer companyId,
        String licensePlate,
        String vehicleType,
        String brand,
        String model,
        Integer year,
        BigDecimal capacity
) {
}

