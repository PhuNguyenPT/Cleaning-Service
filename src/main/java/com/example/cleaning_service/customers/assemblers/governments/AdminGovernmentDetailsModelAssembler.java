package com.example.cleaning_service.customers.assemblers.governments;

import com.example.cleaning_service.customers.dto.governments.GovernmentDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.Government;
import com.example.cleaning_service.customers.mappers.GovernmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminGovernmentDetailsModelAssembler extends RepresentationModelAssemblerSupport<Account, GovernmentDetailsResponseModel> {

    private final GovernmentMapper governmentMapper;

    public AdminGovernmentDetailsModelAssembler(Class<?> controllerClass, Class< GovernmentDetailsResponseModel > resourceType, GovernmentMapper governmentMapper) {
        super(controllerClass, resourceType);
        this.governmentMapper = governmentMapper;
    }

    @Override
    protected @NonNull GovernmentDetailsResponseModel instantiateModel(@NonNull Account account) {
        if (!(account.getCustomer() instanceof Government)) {
            log.error("Account customer {} is not a Government", account.getCustomer());
            throw new InvalidRequestException("Customer is not a Government");
        }
        return governmentMapper.fromGovernmentToGovernmentDetailsResponseModel((Government) account.getCustomer());
    }

    @Override
    public @NonNull GovernmentDetailsResponseModel toModel(@NonNull Account account) {
        return createModelWithId(account.getCustomer().getId(), account, account.getId());
    }
}