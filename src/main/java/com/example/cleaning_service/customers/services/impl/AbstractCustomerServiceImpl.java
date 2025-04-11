package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.CustomerPreferredDay;
import com.example.cleaning_service.customers.mappers.CustomerPreferredDayMapper;
import com.example.cleaning_service.customers.services.AbstractCustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class AbstractCustomerServiceImpl implements AbstractCustomerService {

    private final CustomerPreferredDayMapper customerPreferredDayMapper;

    AbstractCustomerServiceImpl(CustomerPreferredDayMapper customerPreferredDayMapper) {
        this.customerPreferredDayMapper = customerPreferredDayMapper;
    }

    @Transactional
    public void updateAbstractCustomerDetails(AbstractCustomer customer, AbstractCustomerRequest customerDetails) {
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
