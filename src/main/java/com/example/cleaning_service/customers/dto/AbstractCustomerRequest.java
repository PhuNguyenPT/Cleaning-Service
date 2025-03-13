package com.example.cleaning_service.customers.dto;

import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;

import java.util.Set;

public record AbstractCustomerRequest(
        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays
) {
}
