package com.example.cleaning_service.customers.dto.individuals;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.DuplicatedValidatable;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Request DTO for updating an individual customer",
        example = """
        {
          "customerDetails": {
            "loyaltyType": "BRONZE",
            "taxId": "123-45-6789",
            "registrationNumber": "12-3456789",
            "billingAddress": "35 Park Avenue, Apt 15C, New York, NY 10016, US",
            "paymentMethod": "CREDIT",
            "preferredDays": ["TUESDAY", "SATURDAY"]
          },
          "businessEntityDetails": {
            "name": "Rebecca Miller",
            "address": "35 Park Avenue, Apt 15C",
            "phone": "+12123334567",
            "email": "rebecca.miller@example.com",
            "city": "New York",
            "state": "NY",
            "zip": "10016",
            "country": "US",
            "notes": "Keys under doormat. Please text when cleaning is complete."
          }
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record IndividualCustomerUpdateRequest(
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