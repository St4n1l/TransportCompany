package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransportDto(
        Integer id,
        Integer companyId,
        Integer clientId,
        Integer vehicleId,
        Integer driverId,
        String startLocation,
        String endLocation,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String transportType,
        String cargoDescription,
        BigDecimal cargoWeight,
        Integer passengerCount,
        BigDecimal price,
        Boolean isPaid
) {
}

