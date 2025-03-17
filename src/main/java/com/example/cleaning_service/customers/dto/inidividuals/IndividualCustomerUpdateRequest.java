package com.example.cleaning_service.customers.dto.inidividuals;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating an individual customer",
        example = """
        {
          "taxId": "123456789",
          "registrationNumber": "US987654321",
          "billingAddress": "789 Main Street, Apt 12B, Chicago, IL 60616, US",
          "paymentMethod": "CREDIT_CARD",
          "preferredDays": ["TUESDAY", "FRIDAY"],
          "customerName": "John Doe",
          "address": "456 Elm St",
          "phone": "+13125557890",
          "email": "john.doe@example.com",
          "city": "Chicago",
          "state": "IL",
          "zip": "60616",
          "country": "US",
          "notes": "Prefers afternoon appointments"
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record IndividualCustomerUpdateRequest(
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