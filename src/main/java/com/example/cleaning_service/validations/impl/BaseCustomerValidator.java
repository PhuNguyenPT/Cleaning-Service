package com.example.cleaning_service.validations.impl;

import com.example.cleaning_service.busness_entity.BusinessEntity;
import com.example.cleaning_service.customers.enums.ECountryType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public abstract class BaseCustomerValidator<T extends Annotation> implements ConstraintValidator<T, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object enclosingEntity = getRootBean(context);
        if (value == null || value.isEmpty() || !(enclosingEntity instanceof BusinessEntity customer)) {
            return false;
        }

        // Determine validation based on country type
        ECountryType country = customer.getCountryType();
        String regex = getRegexForCountry(country);

        return Pattern.matches(regex, value);
    }

    private Object getRootBean(ConstraintValidatorContext context) {
        try {
            return context.unwrap(jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.class)
                    .addBeanNode()
                    .addConstraintViolation()
                    .unwrap(Object.class);
        } catch (Exception e) {
            return null;  // In case unwrapping fails
        }
    }

    protected abstract String getRegexForCountry(ECountryType country);
}
