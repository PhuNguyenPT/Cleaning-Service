package com.example.cleaning_service.customers.dto;

import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.enums.EAssociationType;
import jakarta.validation.constraints.NotNull;

public record AccountAssociationRequest(
        @NotNull AbstractCustomer customer,
        String notes,
        Boolean isPrimary,
        @NotNull EAssociationType associationType
) {
}
