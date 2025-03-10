package com.example.cleaning_service.validations.impl;

import com.example.cleaning_service.customers.api.ICustomer;
import com.example.cleaning_service.customers.enums.ECountryType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public abstract class BaseCustomerValidator<T extends Annotation> implements ConstraintValidator<T, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Get the enclosing entity (must implement ICustomer)
        Object enclosingEntity = context.unwrap(Object.class);
        if (value == null || value.isEmpty() || !(enclosingEntity instanceof ICustomer customer)) {
            return false;
        }

        // Determine validation based on country type
        ECountryType country = customer.getCountryType();
        String regex = getRegexForCountry(country);

        return Pattern.matches(regex, value);
    }

    protected abstract String getRegexForCountry(ECountryType country);
}
