package com.example.cleaning_service.customers.dto.accounts;

import com.example.cleaning_service.customers.enums.EAssociationType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request model for updating account details")
public record AccountUpdateRequest(
        @Schema(example = "Premium customer since 2023")
        String notes,

        @Schema(example = "true")
        Boolean isPrimary,

        EAssociationType eAssociationType
) {

}
