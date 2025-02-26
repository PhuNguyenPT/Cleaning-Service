package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.ELoyaltyType;
import com.example.cleaning_service.customers.EOrganizationType;
import com.example.cleaning_service.customers.ICustomer;
import jakarta.persistence.*;

@Entity
@Table(name = "governments")
public class Government extends BusinessEntity implements ICustomer {
    private final @Enumerated(EnumType.STRING) @Column(nullable = false)  EOrganizationType organizationType = EOrganizationType.GOVERNMENT;
    private @Enumerated(EnumType.STRING) @Column(nullable = false) ELoyaltyType loyaltyType = ELoyaltyType.STANDARD;

    public Government() {
    }

    public Government(ELoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
    }

    public void setLoyaltyType(ELoyaltyType loyaltyType) {
        this.loyaltyType = loyaltyType;
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