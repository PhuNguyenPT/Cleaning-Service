package com.example.cleaning_service.customers;

import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.ELoyaltyType;
import com.example.cleaning_service.customers.enums.EOrganizationType;

import java.util.Set;

public interface ICustomer {
    String getTaxId();
    String getRegistrationNumber();
    EOrganizationType getOrganizationType();
    default ELoyaltyType getLoyaltyType() {
        return ELoyaltyType.STANDARD;
    };

    String getBillingAddress();
    String getPaymentMethod();
    Set<EDay> getPreferredDays();
}
