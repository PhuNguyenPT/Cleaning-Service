package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AbstractCustomerService {

    @Transactional
    void updateAbstractCustomerDetails(AbstractCustomer customer, @NotNull AbstractCustomerRequest customerDetails) {
        if (customerDetails.billingAddress() != null) {
            customer.setBillingAddress(customerDetails.billingAddress());
        }
        if (customerDetails.paymentMethod() != null) {
            customer.setPaymentMethod(customerDetails.paymentMethod());
        }
        if (customerDetails.preferredDays() != null) {
            customer.setPreferredDays(customerDetails.preferredDays());
        }
    }
}
