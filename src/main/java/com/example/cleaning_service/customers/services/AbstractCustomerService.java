package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;

public interface AbstractCustomerService {
    void updateAbstractCustomerDetails(AbstractCustomer customer, AbstractCustomerRequest customerDetails);
}
