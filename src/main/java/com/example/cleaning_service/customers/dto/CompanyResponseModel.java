package com.example.cleaning_service.customers.dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class CompanyResponseModel extends RepresentationModel<CompanyResponseModel> {
    private final UUID id;
    private final String name;

    public CompanyResponseModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
