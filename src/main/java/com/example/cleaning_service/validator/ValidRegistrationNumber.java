package com.example.cleaning_service.validator;

import com.example.cleaning_service.validator.impl.ValidRegistrationNumberValidator;
import com.example.cleaning_service.validator.impl.ValidTaxIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidRegistrationNumberValidator.class)
public @interface ValidRegistrationNumber {
    String message() default "Invalid Registration number format for the specified country";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
