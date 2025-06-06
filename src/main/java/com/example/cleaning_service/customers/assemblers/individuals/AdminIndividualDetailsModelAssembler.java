package com.example.cleaning_service.customers.assemblers.individuals;

import com.example.cleaning_service.customers.dto.individuals.IndividualCustomerDetailsResponseModel;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.customers.entities.IndividualCustomer;
import com.example.cleaning_service.customers.mappers.IndividualCustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminIndividualDetailsModelAssembler extends RepresentationModelAssemblerSupport<Account, IndividualCustomerDetailsResponseModel> {
    private final IndividualCustomerMapper individualCustomerMapper;

    public AdminIndividualDetailsModelAssembler(Class<?> controllerClass, Class<IndividualCustomerDetailsResponseModel> resourceType, IndividualCustomerMapper individualCustomerMapper) {
        super(controllerClass, resourceType);
        this.individualCustomerMapper = individualCustomerMapper;
    }

    @Override
    protected @NonNull IndividualCustomerDetailsResponseModel instantiateModel(@NonNull Account account) {
        if (!(account.getCustomer() instanceof IndividualCustomer)) {
            log.error("Account customer {} is not an IndividualCustomer", account.getCustomer());
            throw new InvalidRequestException("Customer is not an IndividualCustomer");
        }
        return individualCustomerMapper.fromCustomerToDetailsResponseModel((IndividualCustomer) account.getCustomer());
    }

    @Override
    public @NonNull IndividualCustomerDetailsResponseModel toModel(@NonNull Account account) {
        return createModelWithId(account.getCustomer().getId(), account, account.getId());
    }
}