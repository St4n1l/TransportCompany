package org.example.repository;

import org.example.model.Client;
import org.example.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByCompanyOrderByName(Company company);
    
    List<Client> findByCompanyId(Integer companyId);
}


