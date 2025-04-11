package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.DuplicatedValidatable;

import java.util.function.Function;

public interface CustomerService {
    <T extends DuplicatedValidatable> void checkDuplicatedFields(
            T request,
            Function<String, Boolean> taxIdChecker,
            Function<String, Boolean> registrationNumberChecker,
            Function<String, Boolean> emailChecker);
}
