package com.example.cleaning_service.customers.dto;

import com.example.cleaning_service.customers.enums.EDay;
import com.example.cleaning_service.customers.enums.EPaymentType;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record AbstractCustomerRequest(
        @Size(min = 10, max = 255, message = "Billing address must be between 10 and 255 characters")
        String billingAddress,
        EPaymentType paymentMethod,
        Set<EDay> preferredDays
) {
}
