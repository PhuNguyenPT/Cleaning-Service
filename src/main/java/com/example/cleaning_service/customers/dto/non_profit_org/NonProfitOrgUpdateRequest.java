package com.example.cleaning_service.customers.dto.non_profit_org;

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
        description = "Request DTO for updating a non-profit organization",
        example = """
        {
          "customerDetails": {
            "loyaltyType": "BRONZE",
            "taxId": "52-1693387",
            "registrationNumber": "52-1693387",
            "billingAddress": "789 Charity Lane, Suite 300, Los Angeles, CA 90015, US",
            "paymentMethod": "BANK_TRANSFER",
            "preferredDays": ["TUESDAY", "THURSDAY"]
          },
          "businessEntityDetails": {
            "name": "Helping Hands Foundation",
            "address": "789 Charity Lane, Suite 300",
            "phone": "+13105550123",
            "email": "contact@helpinghands.org",
            "city": "Los Angeles",
            "state": "CA",
            "zip": "90015",
            "country": "US",
            "notes": "Preferred afternoon service."
          }
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record NonProfitOrgUpdateRequest(
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