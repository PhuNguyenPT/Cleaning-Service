package com.example.cleaning_service.customers.dto.non_profit_org;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public final class NonProfitOrgResponseModel extends RepresentationModel<NonProfitOrgResponseModel> {
    private final UUID id;
    private final String name;

    public NonProfitOrgResponseModel(UUID id, String name) {
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
