package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.ValidationException;
import org.example.model.Client;
import org.example.model.Company;
import org.example.repository.ClientRepository;
import org.example.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final Validator validator;

    public Client createClient(Client client, Integer companyId) throws ValidationException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        client.setCompany(company);
        validate(client);
        return clientRepository.save(client);
    }

    public Client getClientById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new org.example.exception.NotFoundException("Client with ID " + id + " not found"));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getClientsByCompanyId(Integer companyId) {
        return clientRepository.findByCompanyId(companyId);
    }

    public Client updateClient(Client client, Integer companyId) throws ValidationException {
        if (client.getId() == null) {
            throw new ValidationException("Client ID is required for update");
        }
        if (!clientRepository.existsById(client.getId())) {
            throw new ValidationException("Client with ID " + client.getId() + " does not exist");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company with ID " + companyId + " does not exist"));
        client.setCompany(company);
        validate(client);
        return clientRepository.save(client);
    }

    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }

    private void validate(Client client) throws ValidationException {
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Client> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(sb.toString());
        }
    }
}
