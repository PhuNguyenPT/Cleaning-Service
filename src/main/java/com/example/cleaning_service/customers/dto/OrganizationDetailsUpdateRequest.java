package com.example.cleaning_service.customers.dto;

import com.example.cleaning_service.validations.ValidRegistrationNumber;
import com.example.cleaning_service.validations.ValidTaxId;

public record OrganizationDetailsUpdateRequest(
        @ValidTaxId String taxId,
        @ValidRegistrationNumber String registrationNumber
) {
}
