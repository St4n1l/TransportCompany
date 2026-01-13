package org.example.mapper;

import org.example.dto.TransportDto;
import org.example.dto.TransportUpsertDto;
import org.example.model.Transport;

public final class TransportMapper {
    private TransportMapper() {}

    public static TransportDto toDto(Transport transport) {
        if (transport == null) return null;
        Integer companyId = transport.getCompany() != null ? transport.getCompany().getId() : null;
        Integer clientId = transport.getClient() != null ? transport.getClient().getId() : null;
        Integer vehicleId = transport.getVehicle() != null ? transport.getVehicle().getId() : null;
        Integer driverId = transport.getDriver() != null ? transport.getDriver().getId() : null;
        return new TransportDto(
                transport.getId(),
                companyId,
                clientId,
                vehicleId,
                driverId,
                transport.getStartLocation(),
                transport.getEndLocation(),
                transport.getStartDate(),
                transport.getEndDate(),
                transport.getTransportType(),
                transport.getCargoDescription(),
                transport.getCargoWeight(),
                transport.getPassengerCount(),
                transport.getPrice(),
                transport.getIsPaid()
        );
    }

    public static void applyUpsert(Transport target, TransportUpsertDto dto) {
        if (target == null || dto == null) return;
        target.setStartLocation(dto.startLocation());
        target.setEndLocation(dto.endLocation());
        target.setStartDate(dto.startDate());
        target.setEndDate(dto.endDate());
        target.setTransportType(dto.transportType());
        target.setCargoDescription(dto.cargoDescription());
        target.setCargoWeight(dto.cargoWeight());
        target.setPassengerCount(dto.passengerCount());
        target.setPrice(dto.price());
        target.setIsPaid(dto.isPaid());
        // NOTE: associations (company/client/vehicle/driver) are set in service (needs repositories).
    }
}

