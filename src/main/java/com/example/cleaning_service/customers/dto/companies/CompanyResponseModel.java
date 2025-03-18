package com.example.cleaning_service.customers.dto.companies;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public final class CompanyResponseModel extends RepresentationModel<CompanyResponseModel> {
    private final UUID id;
    private final String name;

    public CompanyResponseModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }
}
