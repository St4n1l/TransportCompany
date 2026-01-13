package org.example.util;

import org.example.dto.TransportDto;
import org.example.model.Transport;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransportFileIO {
    private static final String DELIMITER = "|";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String serializeTransports(List<Transport> transports) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID|CompanyID|ClientID|VehicleID|DriverID|StartLocation|EndLocation|StartDate|EndDate|TransportType|CargoDescription|CargoWeight|PassengerCount|Price|IsPaid");
        sb.append(System.lineSeparator());
        for (Transport transport : transports) {
            sb.append(formatTransport(transport)).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static String serializeTransportDtos(List<TransportDto> transports) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID|CompanyID|ClientID|VehicleID|DriverID|StartLocation|EndLocation|StartDate|EndDate|TransportType|CargoDescription|CargoWeight|PassengerCount|Price|IsPaid");
        sb.append(System.lineSeparator());
        for (TransportDto transport : transports) {
            sb.append(formatTransportDto(transport)).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static void saveTransportsToFile(List<Transport> transports, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(serializeTransports(transports));
        }
    }

    public static void saveTransportDtosToFile(List<TransportDto> transports, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(serializeTransportDtos(transports));
        }
    }

    public static List<TransportFileRow> parseTransports(String content) throws IOException {
        List<TransportFileRow> transports = new ArrayList<>();
        if (content == null || content.isBlank()) {
            return transports;
        }

        try (BufferedReader reader = new BufferedReader(new StringReader(content))) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("ID")) {
                return transports;
            }
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                TransportFileRow row = parseTransportRow(line);
                if (row != null) {
                    transports.add(row);
                }
            }
        }
        return transports;
    }

    public static List<TransportFileRow> loadTransportsFromFile(String filePath) throws IOException {
        List<TransportFileRow> transports = new ArrayList<>();
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            return transports;
        }
        
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("ID")) {
                return transports;
            }
            
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                TransportFileRow row = parseTransportRow(line);
                if (row != null) {
                    transports.add(row);
                }
            }
        }
        return transports;
    }

    private static String formatTransport(Transport transport) {
        StringBuilder sb = new StringBuilder();
        sb.append(transport.getId() != null ? transport.getId() : "").append(DELIMITER);
        sb.append(transport.getCompany() != null ? transport.getCompany().getId() : "").append(DELIMITER);
        sb.append(transport.getClient() != null ? transport.getClient().getId() : "").append(DELIMITER);
        sb.append(transport.getVehicle() != null ? transport.getVehicle().getId() : "").append(DELIMITER);
        sb.append(transport.getDriver() != null ? transport.getDriver().getId() : "").append(DELIMITER);
        sb.append(transport.getStartLocation() != null ? transport.getStartLocation() : "").append(DELIMITER);
        sb.append(transport.getEndLocation() != null ? transport.getEndLocation() : "").append(DELIMITER);
        sb.append(transport.getStartDate() != null ? transport.getStartDate().format(DATE_FORMATTER) : "").append(DELIMITER);
        sb.append(transport.getEndDate() != null ? transport.getEndDate().format(DATE_FORMATTER) : "").append(DELIMITER);
        sb.append(transport.getTransportType() != null ? transport.getTransportType() : "").append(DELIMITER);
        sb.append(transport.getCargoDescription() != null ? transport.getCargoDescription() : "").append(DELIMITER);
        sb.append(transport.getCargoWeight() != null ? transport.getCargoWeight() : "").append(DELIMITER);
        sb.append(transport.getPassengerCount() != null ? transport.getPassengerCount() : "").append(DELIMITER);
        sb.append(transport.getPrice() != null ? transport.getPrice() : "").append(DELIMITER);
        sb.append(transport.getIsPaid() != null ? transport.getIsPaid() : false);
        return sb.toString();
    }

    private static String formatTransportDto(TransportDto transport) {
        StringBuilder sb = new StringBuilder();
        sb.append(transport.id() != null ? transport.id() : "").append(DELIMITER);
        sb.append(transport.companyId() != null ? transport.companyId() : "").append(DELIMITER);
        sb.append(transport.clientId() != null ? transport.clientId() : "").append(DELIMITER);
        sb.append(transport.vehicleId() != null ? transport.vehicleId() : "").append(DELIMITER);
        sb.append(transport.driverId() != null ? transport.driverId() : "").append(DELIMITER);
        sb.append(transport.startLocation() != null ? transport.startLocation() : "").append(DELIMITER);
        sb.append(transport.endLocation() != null ? transport.endLocation() : "").append(DELIMITER);
        sb.append(transport.startDate() != null ? transport.startDate().format(DATE_FORMATTER) : "").append(DELIMITER);
        sb.append(transport.endDate() != null ? transport.endDate().format(DATE_FORMATTER) : "").append(DELIMITER);
        sb.append(transport.transportType() != null ? transport.transportType() : "").append(DELIMITER);
        sb.append(transport.cargoDescription() != null ? transport.cargoDescription() : "").append(DELIMITER);
        sb.append(transport.cargoWeight() != null ? transport.cargoWeight() : "").append(DELIMITER);
        sb.append(transport.passengerCount() != null ? transport.passengerCount() : "").append(DELIMITER);
        sb.append(transport.price() != null ? transport.price() : "").append(DELIMITER);
        sb.append(transport.isPaid() != null ? transport.isPaid() : false);
        return sb.toString();
    }

    public static TransportFileRow parseTransportRow(String line) {
        try {
            String[] parts = line.split("\\" + DELIMITER, -1);
            if (parts.length < 15) {
                return null;
            }

            Integer companyId = parts[1].isEmpty() ? null : Integer.parseInt(parts[1]);
            Integer clientId = parts[2].isEmpty() ? null : Integer.parseInt(parts[2]);
            Integer vehicleId = parts[3].isEmpty() ? null : Integer.parseInt(parts[3]);
            Integer driverId = parts[4].isEmpty() ? null : Integer.parseInt(parts[4]);

            String startLocation = parts[5];
            String endLocation = parts[6];

            LocalDateTime startDate = parts[7].isEmpty() ? null : LocalDateTime.parse(parts[7], DATE_FORMATTER);
            LocalDateTime endDate = parts[8].isEmpty() ? null : LocalDateTime.parse(parts[8], DATE_FORMATTER);

            String transportType = parts[9];
            String cargoDescription = parts[10];
            BigDecimal cargoWeight = parts[11].isEmpty() ? null : new BigDecimal(parts[11]);
            Integer passengerCount = parts[12].isEmpty() ? null : Integer.parseInt(parts[12]);
            BigDecimal price = parts[13].isEmpty() ? null : new BigDecimal(parts[13]);
            Boolean isPaid = parts[14].isEmpty() ? null : Boolean.parseBoolean(parts[14]);

            return new TransportFileRow(
                    companyId,
                    clientId,
                    vehicleId,
                    driverId,
                    startLocation,
                    endLocation,
                    startDate,
                    endDate,
                    transportType,
                    cargoDescription,
                    cargoWeight,
                    passengerCount,
                    price,
                    isPaid
            );
        } catch (Exception e) {
            System.err.println("Error parsing transport line: " + line);
            e.printStackTrace();
            return null;
        }
    }
}


