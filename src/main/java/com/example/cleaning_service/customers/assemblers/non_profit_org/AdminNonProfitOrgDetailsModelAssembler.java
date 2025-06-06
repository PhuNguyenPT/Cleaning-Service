package com.example.cleaning_service.customers.assemblers.non_profit_org;

import com.example.cleaning_service.customers.dto.non_profit_org.NonProfitOrgDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.NonProfitOrg;
import com.example.cleaning_service.customers.mappers.NonProfitOrgMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminNonProfitOrgDetailsModelAssembler extends RepresentationModelAssemblerSupport<Account, NonProfitOrgDetailsResponseModel> {

    private final NonProfitOrgMapper nonProfitOrgMapper;

    public AdminNonProfitOrgDetailsModelAssembler(Class<?> controllerClass, Class<NonProfitOrgDetailsResponseModel> resourceType, NonProfitOrgMapper nonProfitOrgMapper) {
        super(controllerClass, resourceType);
        this.nonProfitOrgMapper = nonProfitOrgMapper;
    }

    @Override
    protected @NonNull NonProfitOrgDetailsResponseModel instantiateModel(@NonNull Account account) {
        if (!(account.getCustomer() instanceof NonProfitOrg)) {
            log.error("Account customer {} is not an NonProfitOrg", account.getCustomer());
            throw new InvalidRequestException("Customer is not an NonProfitOrg");
        }
        return nonProfitOrgMapper.fromNonProfitOrgToDetailsModel((NonProfitOrg) account.getCustomer());
    }

    @Override
    public @NonNull NonProfitOrgDetailsResponseModel toModel(@NonNull Account account) {
        return createModelWithId(account.getCustomer().getId(), account, account.getId());
    }
}