package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.CustomerPreferredDay;
import com.example.cleaning_service.customers.mappers.CustomerPreferredDayMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AbstractCustomerService {

    private final CustomerPreferredDayMapper customerPreferredDayMapper;

    public AbstractCustomerService(CustomerPreferredDayMapper customerPreferredDayMapper) {
        this.customerPreferredDayMapper = customerPreferredDayMapper;
    }

    @Transactional
    void updateAbstractCustomerDetails(AbstractCustomer customer, AbstractCustomerRequest customerDetails) {
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
