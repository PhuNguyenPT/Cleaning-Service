package com.example.cleaning_service.validations;

import com.example.cleaning_service.validations.impl.TaxIdValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TaxIdValidatorImpl.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaxId {
    String message() default "Invalid Tax ID format for the organization's country";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
