package com.example.cleaning_service.customers.dto.governments;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class GovernmentResponseModel extends RepresentationModel<GovernmentResponseModel> {
    private final UUID id;
    private final String name;

    public GovernmentResponseModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
