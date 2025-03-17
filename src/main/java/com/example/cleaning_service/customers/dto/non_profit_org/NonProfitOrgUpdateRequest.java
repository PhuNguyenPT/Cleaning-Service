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

@Schema(description = "Request DTO for creating a non-profit organization",
        example = """
        {
          "taxId": "456789123",
          "registrationNumber": "USNPO987654",
          "billingAddress": "789 Charity Lane, Suite 200, Los Angeles, CA 90015, US",
          "paymentMethod": "BANK_TRANSFER",
          "preferredDays": ["TUESDAY", "THURSDAY"],
          "organizationName": "Helping Hands Foundation",
          "address": "123 Main St, Los Angeles, CA 90015",
          "phone": "+12135556789",
          "email": "info@helpinghands.org",
          "city": "Los Angeles",
          "state": "CA",
          "zip": "90015",
          "country": "US",
          "notes": "Requires eco-friendly cleaning products."
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record NonProfitOrgUpdateRequest(
        OrganizationDetailsRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable {
    @Override
    public String getRegistrationNumber()
    {
        return organizationDetails.registrationNumber();
    }

    @Override
    public String getTaxId() {
        return organizationDetails.taxId();
    }

    @Override
    public ECountryType getCountry() {
        return businessEntityDetails.country();
    }
}