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
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"company"})
public class Vehicle {
    public enum VehicleType {
        BUS, TRUCK, TANKER, VAN, CAR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "Company ID is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @NotBlank(message = "License plate is required")
    @Column(name = "license_plate", nullable = false, unique = true, length = 50)
    private String licensePlate;
    
    @NotBlank(message = "Vehicle type is required")
    @Column(name = "vehicle_type", nullable = false, length = 50)
    private String vehicleType;
    
    @Column(length = 100)
    private String brand;
    
    @Column(length = 100)
    private String model;
    
    private Integer year;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal capacity;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Vehicle(Company company, String licensePlate, String vehicleType, String brand, String model, Integer year, BigDecimal capacity) {
        this.company = company;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.capacity = capacity;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vehicle vehicle = (Vehicle) o;
        return id != null && id.equals(vehicle.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
