package org.example.repository;

import org.example.model.Company;
import org.example.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByCompanyOrderByLicensePlate(Company company);
    
    List<Vehicle> findByCompanyId(Integer companyId);
}


