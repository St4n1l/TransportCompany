package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;
import org.example.model.*;
import org.example.util.TransportFileIO;
import org.example.util.TransportFileRow;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final CompanyService companyService;
    private final ClientService clientService;
    private final VehicleService vehicleService;
    private final EmployeeService employeeService;
    private final TransportService transportService;
    private final ReportService reportService;

    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void start() {
        while (true) {
            System.out.println();
            System.out.println("=== Transport Company (Console) ===");
            System.out.println("1) Companies");
            System.out.println("2) Clients");
            System.out.println("3) Vehicles");
            System.out.println("4) Employees");
            System.out.println("5) Transports");
            System.out.println("6) Reports");
            System.out.println("7) File (Transports export/import)");
            System.out.println("0) Exit");

            int choice = readInt("Choice: ");
            try {
                switch (choice) {
                    case 1 -> companiesMenu();
                    case 2 -> clientsMenu();
                    case 3 -> vehiclesMenu();
                    case 4 -> employeesMenu();
                    case 5 -> transportsMenu();
                    case 6 -> reportsMenu();
                    case 7 -> fileMenu();
                    case 0 -> {
                        System.out.println("Bye.");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("UNEXPECTED ERROR: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    private void companiesMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Companies ===");
            System.out.println("1) Create");
            System.out.println("2) List");
            System.out.println("3) Update");
            System.out.println("4) Delete");
            System.out.println("5) Sort by name");
            System.out.println("6) Sort by revenue");
            System.out.println("0) Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> {
                    Company company = new Company();
                    company.setName(readString("Name: "));
                    company.setAddress(readString("Address: "));
                    company.setPhone(readString("Phone: "));
                    company.setEmail(readString("Email: "));
                    Company saved = companyService.createCompany(company);
                    System.out.println("Created company id=" + saved.getId());
                }
                case 2 -> companyService.getAllCompanies().forEach(System.out::println);
                case 3 -> {
                    int id = readInt("Company id: ");
                    Company company = companyService.getCompanyById(id);
                    company.setName(readStringDefault("Name", company.getName()));
                    company.setAddress(readStringDefault("Address", company.getAddress()));
                    company.setPhone(readStringDefault("Phone", company.getPhone()));
                    company.setEmail(readStringDefault("Email", company.getEmail()));
                    companyService.updateCompany(company);
                    System.out.println("Updated.");
                }
                case 4 -> {
                    int id = readInt("Company id: ");
                    companyService.deleteCompany(id);
                    System.out.println("Deleted.");
                }
                case 5 -> companyService.getAllCompaniesSortedByName().forEach(System.out::println);
                case 6 -> companyService.getAllCompaniesSortedByRevenue().forEach(System.out::println);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void clientsMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Clients ===");
            System.out.println("1) Create");
            System.out.println("2) List");
            System.out.println("3) List by company");
            System.out.println("4) Update");
            System.out.println("5) Delete");
            System.out.println("0) Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> {
                    int companyId = readInt("Company id: ");
                    Client client = new Client();
                    client.setName(readString("Name: "));
                    client.setContactPerson(readString("Contact person: "));
                    client.setPhone(readString("Phone: "));
                    client.setEmail(readString("Email: "));
                    client.setAddress(readString("Address: "));
                    Client saved = clientService.createClient(client, companyId);
                    System.out.println("Created client id=" + saved.getId());
                }
                case 2 -> clientService.getAllClients().forEach(System.out::println);
                case 3 -> {
                    int companyId = readInt("Company id: ");
                    clientService.getClientsByCompanyId(companyId).forEach(System.out::println);
                }
                case 4 -> {
                    int id = readInt("Client id: ");
                    Client client = clientService.getClientById(id);
                    int companyId = readInt("Company id: ");
                    client.setName(readStringDefault("Name", client.getName()));
                    client.setContactPerson(readStringDefault("Contact person", client.getContactPerson()));
                    client.setPhone(readStringDefault("Phone", client.getPhone()));
                    client.setEmail(readStringDefault("Email", client.getEmail()));
                    client.setAddress(readStringDefault("Address", client.getAddress()));
                    clientService.updateClient(client, companyId);
                    System.out.println("Updated.");
                }
                case 5 -> {
                    int id = readInt("Client id: ");
                    clientService.deleteClient(id);
                    System.out.println("Deleted.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void vehiclesMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Vehicles ===");
            System.out.println("1) Create");
            System.out.println("2) List");
            System.out.println("3) List by company");
            System.out.println("4) Update");
            System.out.println("5) Delete");
            System.out.println("0) Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> {
                    int companyId = readInt("Company id: ");
                    Vehicle v = new Vehicle();
                    v.setLicensePlate(readString("License plate: "));
                    v.setVehicleType(readString("Vehicle type (BUS/TRUCK/TANKER/...): "));
                    v.setBrand(readString("Brand: "));
                    v.setModel(readString("Model: "));
                    v.setYear(readIntNullable("Year (empty = null): "));
                    v.setCapacity(readBigDecimalNullable("Capacity (empty = null): "));
                    Vehicle saved = vehicleService.createVehicle(v, companyId);
                    System.out.println("Created vehicle id=" + saved.getId());
                }
                case 2 -> vehicleService.getAllVehicles().forEach(System.out::println);
                case 3 -> {
                    int companyId = readInt("Company id: ");
                    vehicleService.getVehiclesByCompanyId(companyId).forEach(System.out::println);
                }
                case 4 -> {
                    int id = readInt("Vehicle id: ");
                    Vehicle v = vehicleService.getVehicleById(id);
                    int companyId = readInt("Company id: ");
                    v.setLicensePlate(readStringDefault("License plate", v.getLicensePlate()));
                    v.setVehicleType(readStringDefault("Vehicle type", v.getVehicleType()));
                    v.setBrand(readStringDefault("Brand", v.getBrand()));
                    v.setModel(readStringDefault("Model", v.getModel()));
                    Integer year = readIntNullable("Year (empty = keep): ");
                    if (year != null) v.setYear(year);
                    BigDecimal cap = readBigDecimalNullable("Capacity (empty = keep): ");
                    if (cap != null) v.setCapacity(cap);
                    vehicleService.updateVehicle(v, companyId);
                    System.out.println("Updated.");
                }
                case 5 -> {
                    int id = readInt("Vehicle id: ");
                    vehicleService.deleteVehicle(id);
                    System.out.println("Deleted.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void employeesMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Employees ===");
            System.out.println("1) Create");
            System.out.println("2) List");
            System.out.println("3) List by company");
            System.out.println("4) Sort by qualification");
            System.out.println("5) Sort by salary");
            System.out.println("6) Filter by qualification");
            System.out.println("7) Update");
            System.out.println("8) Delete");
            System.out.println("0) Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> {
                    int companyId = readInt("Company id: ");
                    Employee e = new Employee();
                    e.setFirstName(readString("First name: "));
                    e.setLastName(readString("Last name: "));
                    e.setPhone(readString("Phone: "));
                    e.setEmail(readString("Email: "));
                    e.setPosition(readString("Position (DRIVER/...): "));
                    e.setQualification(readString("Qualification: "));
                    e.setSalary(readBigDecimalNullable("Salary (empty=null): "));
                    e.setHireDate(readLocalDateNullable("Hire date yyyy-MM-dd (empty=null): "));
                    Employee saved = employeeService.createEmployee(e, companyId);
                    System.out.println("Created employee id=" + saved.getId());
                }
                case 2 -> employeeService.getAllEmployees().forEach(System.out::println);
                case 3 -> {
                    int companyId = readInt("Company id: ");
                    employeeService.getEmployeesByCompanyId(companyId).forEach(System.out::println);
                }
                case 4 -> employeeService.getAllEmployeesSortedByQualification().forEach(System.out::println);
                case 5 -> employeeService.getAllEmployeesSortedBySalary().forEach(System.out::println);
                case 6 -> {
                    String q = readString("Qualification contains: ");
                    employeeService.getEmployeesByQualification(q).forEach(System.out::println);
                }
                case 7 -> {
                    int id = readInt("Employee id: ");
                    Employee e = employeeService.getEmployeeById(id);
                    int companyId = readInt("Company id: ");
                    e.setFirstName(readStringDefault("First name", e.getFirstName()));
                    e.setLastName(readStringDefault("Last name", e.getLastName()));
                    e.setPhone(readStringDefault("Phone", e.getPhone()));
                    e.setEmail(readStringDefault("Email", e.getEmail()));
                    e.setPosition(readStringDefault("Position", e.getPosition()));
                    e.setQualification(readStringDefault("Qualification", e.getQualification()));
                    BigDecimal salary = readBigDecimalNullable("Salary (empty=keep): ");
                    if (salary != null) e.setSalary(salary);
                    LocalDate hire = readLocalDateNullable("Hire date yyyy-MM-dd (empty=keep): ");
                    if (hire != null) e.setHireDate(hire);
                    employeeService.updateEmployee(e, companyId);
                    System.out.println("Updated.");
                }
                case 8 -> {
                    int id = readInt("Employee id: ");
                    employeeService.deleteEmployee(id);
                    System.out.println("Deleted.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void transportsMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Transports ===");
            System.out.println("1) Create");
            System.out.println("2) List");
            System.out.println("3) List by company");
            System.out.println("4) Sort by destination");
            System.out.println("5) Filter by destination");
            System.out.println("6) Mark paid");
            System.out.println("7) Update");
            System.out.println("8) Delete");
            System.out.println("0) Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> {
                    int companyId = readInt("Company id: ");
                    int clientId = readInt("Client id: ");
                    Integer vehicleId = readIntNullable("Vehicle id (empty=null): ");
                    Integer driverId = readIntNullable("Driver id (empty=null): ");

                    Transport t = new Transport();
                    t.setStartLocation(readString("Start location: "));
                    t.setEndLocation(readString("End location: "));
                    t.setStartDate(readLocalDateTime("Start date yyyy-MM-dd HH:mm: "));
                    t.setEndDate(readLocalDateTimeNullable("End date yyyy-MM-dd HH:mm (empty=null): "));
                    t.setTransportType(readString("Type (CARGO/PASSENGER): "));
                    t.setCargoDescription(readString("Cargo description: "));
                    t.setCargoWeight(readBigDecimalNullable("Cargo weight (empty=null): "));
                    t.setPassengerCount(readIntNullable("Passenger count (empty=null): "));
                    t.setPrice(readBigDecimal("Price: "));
                    t.setIsPaid(readBooleanNullable("Is paid (true/false, empty=false): ", false));

                    Transport saved = transportService.createTransport(t, companyId, clientId, vehicleId, driverId);
                    System.out.println("Created transport id=" + saved.getId());
                }
                case 2 -> transportService.getAllTransports().forEach(System.out::println);
                case 3 -> {
                    int companyId = readInt("Company id: ");
                    transportService.getTransportsByCompanyId(companyId).forEach(System.out::println);
                }
                case 4 -> transportService.getAllTransportsSortedByDestination().forEach(System.out::println);
                case 5 -> {
                    String d = readString("Destination contains: ");
                    transportService.getTransportsByDestination(d).forEach(System.out::println);
                }
                case 6 -> {
                    int id = readInt("Transport id: ");
                    transportService.markAsPaid(id);
                    System.out.println("Marked paid.");
                }
                case 7 -> {
                    int id = readInt("Transport id: ");
                    Transport t = transportService.getTransportById(id);
                    int companyId = readInt("Company id: ");
                    int clientId = readInt("Client id: ");
                    Integer vehicleId = readIntNullable("Vehicle id (empty=null): ");
                    Integer driverId = readIntNullable("Driver id (empty=null): ");
                    t.setStartLocation(readStringDefault("Start location", t.getStartLocation()));
                    t.setEndLocation(readStringDefault("End location", t.getEndLocation()));
                    // keep start/end date unless user overwrites:
                    LocalDateTime sd = readLocalDateTimeNullable("Start date yyyy-MM-dd HH:mm (empty=keep): ");
                    if (sd != null) t.setStartDate(sd);
                    LocalDateTime ed = readLocalDateTimeNullable("End date yyyy-MM-dd HH:mm (empty=keep): ");
                    if (ed != null) t.setEndDate(ed);
                    t.setTransportType(readStringDefault("Type", t.getTransportType()));
                    t.setCargoDescription(readStringDefault("Cargo description", t.getCargoDescription()));
                    BigDecimal w = readBigDecimalNullable("Cargo weight (empty=keep): ");
                    if (w != null) t.setCargoWeight(w);
                    Integer pc = readIntNullable("Passenger count (empty=keep): ");
                    if (pc != null) t.setPassengerCount(pc);
                    BigDecimal price = readBigDecimalNullable("Price (empty=keep): ");
                    if (price != null) t.setPrice(price);
                    Boolean paid = readBooleanNullable("Is paid (true/false, empty=keep): ", null);
                    if (paid != null) t.setIsPaid(paid);
                    transportService.updateTransport(t, companyId, clientId, vehicleId, driverId);
                    System.out.println("Updated.");
                }
                case 8 -> {
                    int id = readInt("Transport id: ");
                    transportService.deleteTransport(id);
                    System.out.println("Deleted.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void reportsMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Reports ===");
            System.out.println("1) Total transports");
            System.out.println("2) Total revenue");
            System.out.println("3) Driver transport counts (by company)");
            System.out.println("4) Driver revenues (by company)");
            System.out.println("5) Company revenue for period");
            System.out.println("0) Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> System.out.println("Total transports: " + reportService.getTotalTransportCount());
                case 2 -> System.out.println("Total revenue: " + reportService.getTotalRevenue());
                case 3 -> {
                    int companyId = readInt("Company id: ");
                    reportService.getDriverTransportCounts(companyId).forEach((e, count) ->
                            System.out.println(e.getFullName() + " (ID:" + e.getId() + ") -> " + count));
                }
                case 4 -> {
                    int companyId = readInt("Company id: ");
                    reportService.getDriverRevenues(companyId).forEach((e, rev) ->
                            System.out.println(e.getFullName() + " (ID:" + e.getId() + ") -> " + rev));
                }
                case 5 -> {
                    int companyId = readInt("Company id: ");
                    LocalDate start = readLocalDate("Start yyyy-MM-dd: ");
                    LocalDate end = readLocalDate("End yyyy-MM-dd: ");
                    BigDecimal rev = reportService.getCompanyRevenueForPeriod(companyId, start.atStartOfDay(), end.atTime(23, 59, 59));
                    System.out.println("Revenue: " + rev);
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void fileMenu() throws IOException {
        while (true) {
            System.out.println();
            System.out.println("=== File (Transports) ===");
            System.out.println("1) Export all to file");
            System.out.println("2) Export company to file");
            System.out.println("3) Import from file");
            System.out.println("0) Back");
            int c = readInt("Choice: ");
            switch (c) {
                case 1 -> {
                    String path = readString("File path: ");
                    String resolved = resolveExportPath(path, "transports.txt");
                    TransportFileIO.saveTransportsToFile(transportService.getAllTransports(), resolved);
                    System.out.println("Exported.");
                }
                case 2 -> {
                    int companyId = readInt("Company id: ");
                    String path = readString("File path: ");
                    String resolved = resolveExportPath(path, "transports-company-" + companyId + ".txt");
                    TransportFileIO.saveTransportsToFile(transportService.getTransportsByCompanyId(companyId), resolved);
                    System.out.println("Exported.");
                }
                case 3 -> {
                    String path = readString("File path: ");
                    List<TransportFileRow> rows = TransportFileIO.loadTransportsFromFile(path);
                    int imported = 0;
                    for (TransportFileRow row : rows) {
                        Transport t = new Transport();
                        t.setStartLocation(row.startLocation());
                        t.setEndLocation(row.endLocation());
                        t.setStartDate(row.startDate());
                        t.setEndDate(row.endDate());
                        t.setTransportType(row.transportType());
                        t.setCargoDescription(row.cargoDescription());
                        t.setCargoWeight(row.cargoWeight());
                        t.setPassengerCount(row.passengerCount());
                        t.setPrice(row.price());
                        t.setIsPaid(row.isPaid());
                        transportService.createTransport(t, row.companyId(), row.clientId(), row.vehicleId(), row.driverId());
                        imported++;
                    }
                    System.out.println("Imported transports: " + imported);
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private String resolveExportPath(String userPath, String defaultFileName) throws IOException {
        if (userPath == null || userPath.isBlank()) {
            throw new IOException("Empty path.");
        }
        Path p = Paths.get(userPath.trim());
        if (Files.exists(p) && Files.isDirectory(p)) {
            return p.resolve(defaultFileName).toString();
        }
        // If it looks like a directory that doesn't exist (no extension), create it and use default file name
        String s = userPath.trim();
        if (!s.contains(".") && !s.endsWith("\\") && !s.endsWith("/")) {
            // allow user to pass directory without trailing slash
            Path dir = p;
            Files.createDirectories(dir);
            return dir.resolve(defaultFileName).toString();
        }
        // Ensure parent directories exist
        Path parent = p.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        return p.toString();
    }

    // ---- input helpers (centralized = “critical case” safety) ----
    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String readStringDefault(String label, String current) {
        System.out.print(label + " [" + (current == null ? "" : current) + "]: ");
        String v = scanner.nextLine().trim();
        return v.isEmpty() ? current : v;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Enter a number.");
            }
        }
    }

    private Integer readIntNullable(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using null.");
            return null;
        }
    }

    private BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Enter a decimal number.");
            }
        }
    }

    private BigDecimal readBigDecimalNullable(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            System.out.println("Invalid number. Using null.");
            return null;
        }
    }

    private LocalDate readLocalDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return LocalDate.parse(s, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Format: yyyy-MM-dd");
            }
        }
    }

    private LocalDate readLocalDateNullable(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return LocalDate.parse(s, DATE_FMT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date. Using null.");
            return null;
        }
    }

    private LocalDateTime readLocalDateTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(s, DT_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("Format: yyyy-MM-dd HH:mm");
            }
        }
    }

    private LocalDateTime readLocalDateTimeNullable(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        try {
            return LocalDateTime.parse(s, DT_FMT);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date/time. Using null.");
            return null;
        }
    }

    private Boolean readBooleanNullable(String prompt, Boolean defaultValue) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return defaultValue;
        return Boolean.parseBoolean(s);
    }
}


