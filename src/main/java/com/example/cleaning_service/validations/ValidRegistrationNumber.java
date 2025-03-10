package com.example.cleaning_service.validations;

import com.example.cleaning_service.validations.impl.RegistrationNumberValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RegistrationNumberValidatorImpl.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegistrationNumber {
    String message() default "Invalid registration number format for the organization's country";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
