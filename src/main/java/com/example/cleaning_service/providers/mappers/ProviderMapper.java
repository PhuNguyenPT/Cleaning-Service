package com.example.cleaning_service.providers.mappers;

import com.example.cleaning_service.providers.dtos.ProviderDetailsModel;
import com.example.cleaning_service.providers.dtos.ProviderModel;
import com.example.cleaning_service.providers.dtos.ProviderRequest;
import com.example.cleaning_service.providers.entities.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {
    public Provider fromProviderRequest(ProviderRequest providerRequest) {
        return Provider.builder()
                // From BusinessEntity fields
                .name(providerRequest.name())
                .address(providerRequest.address())
                .phone(providerRequest.phone())
                .email(providerRequest.email())
                .city(providerRequest.city())
                .state(providerRequest.state())
                .zip(providerRequest.zip())
                .country(providerRequest.country())
                .notes(providerRequest.notes())

                // Provider-specific fields
                .serviceAreas(providerRequest.serviceAreas())
                .serviceTypes(providerRequest.serviceTypes())
                .hourlyRate(providerRequest.hourlyRate())
                .minimumServiceHours(providerRequest.minimumServiceHours())
                .teamSize(providerRequest.teamSize())
                .equipment(providerRequest.equipment())
                .certifications(providerRequest.certifications())
                .insurancePolicyNumber(providerRequest.insurancePolicyNumber())
                .insuranceProvider(providerRequest.insuranceProvider())
                .insuranceCoverageAmount(providerRequest.insuranceCoverageAmount())
                .yearsOfExperience(providerRequest.yearsOfExperience())
                .maxTravelDistance(providerRequest.maxTravelDistance())
                .cancellationPolicy(providerRequest.cancellationPolicy())
                .specialOffers(providerRequest.specialOffers())
                .build();
    }

    public ProviderModel toProviderModel(Provider provider) {
        return ProviderModel.builder()
                .id(provider.getId())
                .name(provider.getName())
                .build();
    }

    public ProviderDetailsModel toProviderDetailsModel(Provider provider) {
        return ProviderDetailsModel.builder()
                // From BusinessEntity fields
                .id(provider.getId())
                .name(provider.getName())
                .address(provider.getAddress())
                .phone(provider.getPhone())
                .email(provider.getEmail())
                .city(provider.getCity())
                .state(provider.getState())
                .zip(provider.getZip())
                .country(provider.getCountry())
                .notes(provider.getNotes())

                // Provider-specific fields
                .serviceAreas(provider.getServiceAreas())
                .serviceTypes(provider.getServiceTypes())
                .hourlyRate(provider.getHourlyRate())
                .minimumServiceHours(provider.getMinimumServiceHours())
                .availability(provider.getAvailability())
                .teamSize(provider.getTeamSize())
                .equipment(provider.getEquipment())
                .certifications(provider.getCertifications())
                .insurancePolicyNumber(provider.getInsurancePolicyNumber())
                .insuranceProvider(provider.getInsuranceProvider())
                .insuranceCoverageAmount(provider.getInsuranceCoverageAmount())
                .yearsOfExperience(provider.getYearsOfExperience())
                .averageRating(provider.getAverageRating())
                .totalReviews(provider.getTotalReviews())
                .backgroundCheckVerified(provider.getBackgroundCheckVerified())
                .maxTravelDistance(provider.getMaxTravelDistance())
                .cancellationPolicy(provider.getCancellationPolicy())
                .specialOffers(provider.getSpecialOffers())
                .build();
    }
}
