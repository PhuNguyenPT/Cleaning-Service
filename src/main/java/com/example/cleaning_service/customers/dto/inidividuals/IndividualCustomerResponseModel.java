package com.example.cleaning_service.customers.dto.inidividuals;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class IndividualCustomerResponseModel extends RepresentationModel<IndividualCustomerResponseModel> {
    private final UUID id;
    private final String name;

    public IndividualCustomerResponseModel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
