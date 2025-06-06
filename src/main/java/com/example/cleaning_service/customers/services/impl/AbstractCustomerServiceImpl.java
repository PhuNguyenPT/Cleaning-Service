package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.mappers.CustomerPreferredDayMapper;
import com.example.cleaning_service.customers.services.AbstractCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
class AbstractCustomerServiceImpl implements AbstractCustomerService {

    private final CustomerPreferredDayMapper customerPreferredDayMapper;

    AbstractCustomerServiceImpl(CustomerPreferredDayMapper customerPreferredDayMapper) {
        this.customerPreferredDayMapper = customerPreferredDayMapper;
    }

    @Transactional
    public void updateAbstractCustomerDetails(AbstractCustomer customer, AbstractCustomerRequest customerDetails) {
        if (customerDetails.loyaltyType() != null) {
            customer.setLoyaltyType(customerDetails.loyaltyType());
        }
        if (customerDetails.taxId() != null) {
            customer.setTaxId(customerDetails.taxId());
        }
        if (customerDetails.registrationNumber() != null) {
            customer.setRegistrationNumber(customerDetails.registrationNumber());
        }
        if (customerDetails.billingAddress() != null) {
            customer.setBillingAddress(customerDetails.billingAddress());
        }
        if (customerDetails.paymentMethod() != null) {
            customer.setPaymentMethod(customerDetails.paymentMethod());
        }
        if (customerDetails.preferredDays() != null) {
            Set<CustomerPreferredDay> updatedDays = customerPreferredDayMapper
                    .fromEDaysToCustomerPreferredDays(customerDetails.preferredDays());
            customer.getPreferredDays().clear();
            customer.getPreferredDays().addAll(updatedDays);
        }
    }
}
