package com.example.cleaning_service.customers.dto.accounts;

import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.enums.EAssociationType;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
        @NotNull AbstractCustomer customer,
        String notes,
        Boolean isPrimary,
        @NotNull EAssociationType associationType
) {
}
