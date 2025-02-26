package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.ELoyaltyType;
import com.example.cleaning_service.customers.ICustomer;
import com.example.cleaning_service.customers.EOrganizationType;
import jakarta.persistence.*;

@Entity
@Table(name = "individual_customers")
public class IndividualCustomer extends BusinessEntity implements ICustomer {
    private final @Enumerated(EnumType.STRING) @Column(nullable = false) EOrganizationType organizationType = EOrganizationType.INDIVIDUAL;
    private final @Enumerated(EnumType.STRING) @Column(nullable = false) ELoyaltyType loyaltyType = ELoyaltyType.STANDARD;

    private Boolean isActive;

    public IndividualCustomer(Boolean isActive) {
        this.isActive = isActive;
    }

    public IndividualCustomer() {
        this.isActive = true;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public EOrganizationType getOrganizationType() {
        return this.organizationType;
    }

    @Override
    public ELoyaltyType getLoyaltyType() {
        return this.loyaltyType;
    }
}