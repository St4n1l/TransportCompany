package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"company", "client", "vehicle", "driver"})
public class Transport {
    public enum TransportType {
        CARGO, PASSENGER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "Company ID is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @NotNull(message = "Client ID is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Employee driver;
    
    @NotBlank(message = "Start location is required")
    @Column(name = "start_location", nullable = false, length = 255)
    private String startLocation;
    
    @NotBlank(message = "End location is required")
    @Column(name = "end_location", nullable = false, length = 255)
    private String endLocation;
    
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @NotBlank(message = "Transport type is required")
    @Column(name = "transport_type", nullable = false, length = 50)
    private String transportType; // CARGO or PASSENGER
    
    @Column(name = "cargo_description", length = 500)
    private String cargoDescription;
    
    @Column(name = "cargo_weight", precision = 10, scale = 2)
    private BigDecimal cargoWeight; // Only for cargo
    
    @Column(name = "passenger_count")
    private Integer passengerCount; // Only for passengers
    
    @NotNull(message = "Price is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "is_paid")
    private Boolean isPaid;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isPaid == null) {
            isPaid = false;
        }
    }

    {
        this.isPaid = false;
    }

    public Transport(Company company, Client client, Vehicle vehicle, Employee driver,
                     String startLocation, String endLocation, LocalDateTime startDate, LocalDateTime endDate,
                     String transportType, String cargoDescription, BigDecimal cargoWeight, Integer passengerCount,
                     BigDecimal price, Boolean isPaid) {
        this.company = company;
        this.client = client;
        this.vehicle = vehicle;
        this.driver = driver;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.transportType = transportType;
        this.cargoDescription = cargoDescription;
        this.cargoWeight = cargoWeight;
        this.passengerCount = passengerCount;
        this.price = price;
        this.isPaid = isPaid != null ? isPaid : false;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transport transport = (Transport) o;
        return id != null && id.equals(transport.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
