package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.ELoyaltyType;
import com.example.cleaning_service.customers.EOrganizationType;
import com.example.cleaning_service.customers.ICustomer;
import jakarta.persistence.*;

@Entity
@Table(name = "non_profit_org")
public class NonProfitOrg extends BusinessEntity implements ICustomer {
    private final @Enumerated(EnumType.STRING) @Column(nullable = false) EOrganizationType organizationType = EOrganizationType.NON_PROFIT;
    private @Enumerated(EnumType.STRING) @Column(nullable = false) ELoyaltyType loyaltyType = ELoyaltyType.STANDARD;

    public NonProfitOrg() {
    }

    public NonProfitOrg(ELoyaltyType loyaltyType) {
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