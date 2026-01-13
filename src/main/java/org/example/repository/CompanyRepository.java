package org.example.repository;

import org.example.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findAllByOrderByName();

    List<Company> findAllByOrderByRevenueDesc();
}


