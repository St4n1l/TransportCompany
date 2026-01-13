package org.example.repository;

import org.example.model.Company;
import org.example.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByCompanyOrderByLastNameAscFirstNameAsc(Company company);
    
    List<Employee> findByCompanyId(Integer companyId);
    
    @Query("SELECT e FROM Employee e ORDER BY e.qualification NULLS LAST, e.lastName, e.firstName")
    List<Employee> findAllOrderByQualification();
    
    @Query("SELECT e FROM Employee e ORDER BY e.salary DESC NULLS LAST")
    List<Employee> findAllOrderBySalaryDesc();
    
    List<Employee> findByQualificationContainingIgnoreCase(String qualification);
}


