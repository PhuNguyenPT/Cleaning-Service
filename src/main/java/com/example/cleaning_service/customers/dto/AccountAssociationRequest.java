package com.example.cleaning_service.customers.dto;

import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.security.entities.user.User;

public record AccountAssociationRequest(
        User user,
        AbstractCustomer customer
) {
}
