package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.customers.dto.DuplicatedValidatable;
import com.example.cleaning_service.customers.services.CustomerService;
import com.example.cleaning_service.exceptions.DuplicateFieldsException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
class CustomerServiceImpl implements CustomerService {

    @Transactional
    @Override
    public <T extends DuplicatedValidatable> void checkDuplicatedFields(
            T request,
            Function<String, Boolean> taxIdChecker,
            Function<String, Boolean> registrationNumberChecker,
            Function<String, Boolean> emailChecker) {

        Map<String, String> duplicateFields = new HashMap<>();

        if (taxIdChecker.apply(request.taxId())) {
            duplicateFields.put("taxId", "Entity with this tax ID already exists");
        }

        if (registrationNumberChecker.apply(request.registrationNumber())) {
            duplicateFields.put("registrationNumber", "Entity with this registration number already exists");
        }

        if (emailChecker.apply(request.email())) {
            duplicateFields.put("email", "Entity with this email already exists");
        }

        if (!duplicateFields.isEmpty()) {
            throw new DuplicateFieldsException(duplicateFields);
        }
    }
}

