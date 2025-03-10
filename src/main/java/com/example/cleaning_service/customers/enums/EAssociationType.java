package com.example.cleaning_service.customers.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum EAssociationType {
    OWNER("Owner of the account"),
    MANAGER("Manager with permissions"),
    REPRESENTATIVE("Authorized representative");

    private final String description;

    EAssociationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
