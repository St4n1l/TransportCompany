package org.example.mapper;

import org.example.dto.ClientDto;
import org.example.dto.ClientUpsertDto;
import org.example.model.Client;

public final class ClientMapper {
    private ClientMapper() {}

    public static ClientDto toDto(Client client) {
        if (client == null) return null;
        Integer companyId = client.getCompany() != null ? client.getCompany().getId() : null;
        return new ClientDto(
                client.getId(),
                companyId,
                client.getName(),
                client.getContactPerson(),
                client.getPhone(),
                client.getEmail(),
                client.getAddress()
        );
    }

    public static void applyUpsert(Client target, ClientUpsertDto dto) {
        if (target == null || dto == null) return;
        target.setName(dto.name());
        target.setContactPerson(dto.contactPerson());
        target.setPhone(dto.phone());
        target.setEmail(dto.email());
        target.setAddress(dto.address());
    }
}

