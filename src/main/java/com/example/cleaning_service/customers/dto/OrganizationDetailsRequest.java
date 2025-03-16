package com.example.cleaning_service.customers.dto;

public record OrganizationDetailsRequest(
        String taxId,
        String registrationNumber
) {
}
