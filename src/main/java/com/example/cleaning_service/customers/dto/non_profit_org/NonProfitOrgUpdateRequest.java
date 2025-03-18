package com.example.cleaning_service.customers.dto.non_profit_org;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
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
          "organizationDetails": {
            "taxId": "52-1693387",
            "registrationNumber": "52-1693387"
          },
          "customerDetails": {
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
        OrganizationDetailsRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable
{
    @Schema(hidden = true)
    @Override
    public String getRegistrationNumber()
    {
        return organizationDetails.registrationNumber();
    }

    @Schema(hidden = true)
    @Override
    public String getTaxId() {
        return organizationDetails.taxId();
    }

    @Schema(hidden = true)
    @Override
    public ECountryType getCountry() {
        return businessEntityDetails.country();
    }
}