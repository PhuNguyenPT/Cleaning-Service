package com.example.cleaning_service.providers.dtos;

import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.providers.enums.EServiceType;
import com.example.cleaning_service.providers.entities.ProviderAvailability;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class ProviderDetailsModel extends RepresentationModel<ProviderDetailsModel> {
    // fields from BusinessEntity
    private final UUID id;
    private final String name;
    private final String address;
    private final String phone;
    private final String email;
    private final String city;
    private final String state;
    private final String zip;
    private final ECountryType country;
    private final String notes;

    // fields from the Provider entity
    private final Set<String> serviceAreas;
    private final Set<EServiceType> serviceTypes;
    private final BigDecimal hourlyRate;
    private final Integer minimumServiceHours;
    private final Set<ProviderAvailability> availability;
    private final Integer teamSize;
    private final Set<String> equipment;
    private final Set<String> certifications;
    private final String insurancePolicyNumber;
    private final String insuranceProvider;
    private final BigDecimal insuranceCoverageAmount;
    private final Integer yearsOfExperience;
    private final Double averageRating;
    private final Integer totalReviews;
    private final Boolean backgroundCheckVerified;
    private final Integer maxTravelDistance;
    private final String cancellationPolicy;
    private final String specialOffers;
}
