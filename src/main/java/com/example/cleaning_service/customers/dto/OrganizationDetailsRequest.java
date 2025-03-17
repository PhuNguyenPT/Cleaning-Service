package com.example.cleaning_service.customers.dto;

import jakarta.validation.constraints.NotBlank;

public record OrganizationDetailsRequest(
        @NotBlank String taxId,
        @NotBlank String registrationNumber
) {
}
