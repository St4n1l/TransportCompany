package org.example.dto;

import java.math.BigDecimal;

public record VehicleDto(
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

