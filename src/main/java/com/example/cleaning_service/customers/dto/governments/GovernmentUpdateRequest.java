package com.example.cleaning_service.customers.dto.governments;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.DuplicatedValidatable;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        description = "Request DTO for updating a government entity",
        example = """
        {
          "contractorName": "Sarah Johnson",
          "departmentName": "Transportation",
          "isTaxExempt": true,
          "requiresEmergencyCleaning": false,
          "customerDetails": {
            "taxId": "12-3456789",
            "registrationNumber": "12-3456789",
            "billingAddress": "1200 New Jersey Ave SE, Washington, DC 20590, US",
            "paymentMethod": "BANK_TRANSFER",
            "preferredDays": ["MONDAY", "WEDNESDAY"]
          },
          "businessEntityDetails": {
            "name": "Department of Transportation",
            "address": "1200 New Jersey Ave SE",
            "phone": "+12023664000",
            "email": "contact@dot.gov",
            "city": "Washington",
            "state": "DC",
            "zip": "20590",
            "country": "US",
            "notes": "Special security access required. Please schedule cleaning after business hours."
          }
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record GovernmentUpdateRequest (
        @Size(min = 2, max = 100, message = "Contractor name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Contractor name must contain only letters, spaces, and hyphens")
        @Schema(example = "John Smith")
        String contractorName,
        @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s-]+$", message = "Department name must contain only letters, spaces, and hyphens")
        @Schema(example = "Public Works")
        String departmentName,
        Boolean isTaxExempt,
        Boolean requiresEmergencyCleaning,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable, DuplicatedValidatable
{
    @Schema(hidden = true)
    @Override
    public String registrationNumber() {
        return customerDetails.registrationNumber();
    }

    @Override
    public String email() {
        return businessEntityDetails.email();
    }

    @Schema(hidden = true)
    @Override
    public String taxId() {
        return customerDetails.taxId();
    }

    @Schema(hidden = true)
    @Override
    public ECountryType country() {
        return businessEntityDetails.country();
    }
}