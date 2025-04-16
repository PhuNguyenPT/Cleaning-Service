package com.example.cleaning_service.providers.entities;

import com.example.cleaning_service.commons.BusinessEntity;
import com.example.cleaning_service.providers.enums.EServiceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "providers", schema = "provider")
@SuperBuilder // Use SuperBuilder since Provider extends BusinessEntity
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider extends BusinessEntity {

    @ElementCollection
    @CollectionTable(name = "provider_service_areas", joinColumns = @JoinColumn(name = "provider_id"), schema = "provider")
    @Column(name = "service_area")
    @Builder.Default
    private Set<String> serviceAreas = new HashSet<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "provider_service_types", joinColumns = @JoinColumn(name = "provider_id"), schema = "provider")
    @Column(name = "service_type")
    @Builder.Default
    private Set<EServiceType> serviceTypes = new HashSet<>();

    @Column(name = "hourly_rate")
    @Builder.Default
    private BigDecimal hourlyRate = BigDecimal.ZERO;

    @Column(name = "minimum_service_hours")
    @Builder.Default
    private Integer minimumServiceHours = 1;

    @ElementCollection
    @CollectionTable(name = "provider_availability", joinColumns = @JoinColumn(name = "provider_id"), schema = "provider")
    @Builder.Default
    private Set<ProviderAvailability> availability = new HashSet<>();

    @Column(name = "team_size")
    @Builder.Default
    private Integer teamSize = 1;

    @ElementCollection
    @CollectionTable(name = "provider_equipment", joinColumns = @JoinColumn(name = "provider_id"), schema = "provider")
    @Column(name = "equipment")
    @Builder.Default
    private Set<String> equipment = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "provider_certifications", joinColumns = @JoinColumn(name = "provider_id"), schema = "provider")
    @Column(name = "certification")
    @Builder.Default
    private Set<String> certifications = new HashSet<>();

    @Column(name = "insurance_policy_number")
    private String insurancePolicyNumber;

    @Column(name = "insurance_provider")
    private String insuranceProvider;

    @Column(name = "insurance_coverage_amount")
    @Builder.Default
    private BigDecimal insuranceCoverageAmount = BigDecimal.ZERO;

    @Column(name = "years_of_experience")
    @Builder.Default
    private Integer yearsOfExperience = 0;

    @Column(name = "average_rating")
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "background_check_verified")
    @Builder.Default
    private Boolean backgroundCheckVerified = false;

    @Column(name = "max_travel_distance")
    @Builder.Default
    private Integer maxTravelDistance = 0;

    @Column(name = "cancellation_policy")
    private String cancellationPolicy;

    @Column(name = "special_offers")
    private String specialOffers;

    @Override
    public String toString() {
        return "Provider{" +
                "serviceAreas=" + serviceAreas +
                ", serviceTypes=" + serviceTypes +
                ", hourlyRate=" + hourlyRate +
                ", minimumServiceHours=" + minimumServiceHours +
                ", availability=" + availability +
                ", teamSize=" + teamSize +
                ", equipment=" + equipment +
                ", certifications=" + certifications +
                ", insurancePolicyNumber='" + insurancePolicyNumber + '\'' +
                ", insuranceProvider='" + insuranceProvider + '\'' +
                ", insuranceCoverageAmount=" + insuranceCoverageAmount +
                ", yearsOfExperience=" + yearsOfExperience +
                ", averageRating=" + averageRating +
                ", totalReviews=" + totalReviews +
                ", backgroundCheckVerified=" + backgroundCheckVerified +
                ", maxTravelDistance=" + maxTravelDistance +
                ", cancellationPolicy='" + cancellationPolicy + '\'' +
                ", specialOffers='" + specialOffers + '\'' +
                super.toString() +
                '}';
    }
}