package com.example.cleaning_service.customers.dto.companies;

import com.example.cleaning_service.commons.BusinessEntityRequest;
import com.example.cleaning_service.customers.dto.AbstractCustomerRequest;
import com.example.cleaning_service.customers.dto.OrganizationDetailsRequest;
import com.example.cleaning_service.customers.enums.ECompanyType;
import com.example.cleaning_service.customers.enums.ECountryType;
import com.example.cleaning_service.validator.IRegistrationNumberIdentifiable;
import com.example.cleaning_service.validator.ITaxIdentifiable;
import com.example.cleaning_service.validator.ValidRegistrationNumber;
import com.example.cleaning_service.validator.ValidTaxId;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating a company",
        example = """
        {
          "companyType": "START_UP",
          "taxId": "123456789",
          "registrationNumber": "US12345678",
          "billingAddress": "123 Finance Avenue, Suite 500, San Francisco, CA 94105, US",
          "paymentMethod": "CASH",
          "preferredDays": ["MONDAY"],
          "companyName": "TechCorp Inc.",
          "address": "456 Elm St",
          "phone": "+18005551234",
          "email": "contact@techcorp.com",
          "city": "New York",
          "state": "NY",
          "zip": "10001",
          "country": "US",
          "notes": "Preferred customer, requires monthly invoicing."
        }
        """
)
@ValidTaxId
@ValidRegistrationNumber
public record CompanyUpdateRequest(
        ECompanyType companyType,

        OrganizationDetailsRequest organizationDetails,

        AbstractCustomerRequest customerDetails,

        BusinessEntityRequest businessEntityDetails
) implements ITaxIdentifiable, IRegistrationNumberIdentifiable
{
    @Override
    public String getRegistrationNumber() {
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
