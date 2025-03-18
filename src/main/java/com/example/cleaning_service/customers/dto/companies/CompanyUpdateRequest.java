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

@Schema(
        description = "Request DTO for updating a company",
        example = """
        {
          "companyType": "START_UP",
          "organizationDetails": {
            "taxId": "12-3456789",
            "registrationNumber": "12-3456789",
          },
          "customerDetails": {
            "billingAddress": "123 Finance Avenue, Suite 500, San Francisco, CA 94105, US",
            "paymentMethod": "CASH",
            "preferredDays": ["MONDAY", "WEDNESDAY"]
          },
          "businessEntityDetails": {
            "name": "TechCorp Inc.",
            "address": "456 Elm St",
            "phone": "+18005551234",
            "email": "contact@techcorp.com",
            "city": "New York",
            "state": "NY",
            "zip": "10001",
            "country": "US",
            "notes": "Preferred customer, requires monthly invoicing."
          }
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
    @Schema(hidden = true)
    @Override
    public String getRegistrationNumber() {
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
