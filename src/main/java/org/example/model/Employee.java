package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"company"})
public class Employee {
    public enum Position {
        DRIVER, MANAGER, DISPATCHER, MECHANIC
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotNull(message = "Company ID is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(length = 50)
    private String phone;
    
    @Email(message = "Invalid email format")
    @Column(length = 255)
    private String email;
    
    @NotBlank(message = "Position is required")
    @Column(nullable = false, length = 100)
    private String position;
    
    @Column(length = 500)
    private String qualification; // e.g., "flammable goods", "12+ passengers"
    
    @Column(precision = 10, scale = 2)
    private BigDecimal salary;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Employee(Company company, String firstName, String lastName, String phone, String email, String position, String qualification, BigDecimal salary, LocalDate hireDate) {
        this.company = company;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.position = position;
        this.qualification = qualification;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Employee employee = (Employee) o;
        return id != null && id.equals(employee.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}
