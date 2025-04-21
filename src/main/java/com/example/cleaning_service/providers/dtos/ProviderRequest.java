package com.example.cleaning_service.providers.dtos;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.providers.enums.EServiceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record ProviderRequest(
        // Basic info fromProviderRequest BusinessEntity
        @NotBlank(message = "Name is required")
        String name,
        String address,
        String phone,
        String email,
        String city,
        String state,
        String zip,
        ECountryType country,
        @Size(max = 1000, message = "Notes must not exceed 1000 characters")
        String notes,

        // Provider-specific fields
        Set<String> serviceAreas,
        Set<EServiceType> serviceTypes,
        @DecimalMin(value = "0.0", inclusive = false, message = "Hourly rate must be positive")
        BigDecimal hourlyRate,
        @Min(value = 1, message = "Minimum service hours must be at least 1")
        Integer minimumServiceHours,
        @Min(value = 1, message = "Team size must be at least 1")
        Integer teamSize,
        Set<String> equipment,
        Set<String> certifications,
        @Size(max = 255, message = "Insurance policy number must not exceed 255 characters")
        String insurancePolicyNumber,
        @Size(max = 255, message = "Insurance provider must not exceed 255 characters")
        String insuranceProvider,
        @DecimalMin(value = "0.0", message = "Insurance coverage amount must be zero or positive")
        BigDecimal insuranceCoverageAmount,
        @Min(value = 0, message = "Years of experience must be zero or positive")
        Integer yearsOfExperience,
        @Min(value = 0, message = "Max travel distance must be zero or positive")
        Integer maxTravelDistance,
        @Size(max = 1000, message = "Cancellation policy must not exceed 1000 characters")
        String cancellationPolicy,
        @Size(max = 1000, message = "Special offers must not exceed 1000 characters")
        String specialOffers
) {
}