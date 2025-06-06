package com.example.cleaning_service.customers.assemblers.companies;

import com.example.cleaning_service.customers.dto.companies.CompanyDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.Company;
import com.example.cleaning_service.customers.mappers.CompanyMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminCompanyDetailsModelAssembler extends RepresentationModelAssemblerSupport<Account, CompanyDetailsResponseModel> {
    private final CompanyMapper companyMapper;

    public AdminCompanyDetailsModelAssembler(Class<?> controllerClass, Class<CompanyDetailsResponseModel> resourceType, CompanyMapper companyMapper) {
        super(controllerClass, resourceType);
        this.companyMapper = companyMapper;
    }

    @Override
    protected @NonNull CompanyDetailsResponseModel instantiateModel(@NonNull Account account) {
        if (!(account.getCustomer() instanceof Company)) {
            log.error("Account customer {} is not a Company", account.getCustomer());
            throw new InvalidRequestException("Customer is not a Company");
        }
        return companyMapper.fromCompanyToCompanyDetailsResponseModel((Company) account.getCustomer());
    }

    @Override
    public @NonNull CompanyDetailsResponseModel toModel(@NonNull Account account) {
        return createModelWithId(account.getCustomer().getId(), account, account.getId());
    }
}