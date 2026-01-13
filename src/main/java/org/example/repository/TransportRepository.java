package org.example.repository;

import org.example.model.Company;
import org.example.model.Employee;
import org.example.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Integer> {
    List<Transport> findByCompanyOrderByStartDateDesc(Company company);
    
    List<Transport> findByCompanyId(Integer companyId);
    
    List<Transport> findByDriver(Employee driver);
    
    List<Transport> findByDriverId(Integer driverId);
    
    @Query("SELECT t FROM Transport t WHERE t.endLocation ILIKE %:destination% ORDER BY t.startDate DESC")
    List<Transport> findByDestination(@Param("destination") String destination);
    
    @Query("SELECT t FROM Transport t ORDER BY t.endLocation, t.startDate DESC")
    List<Transport> findAllOrderByDestination();
    
    @Query("SELECT t FROM Transport t WHERE t.company.id = :companyId AND t.startDate >= :startDate AND t.startDate <= :endDate ORDER BY t.startDate")
    List<Transport> findByCompanyIdAndDateRange(@Param("companyId") Integer companyId, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
}


