package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.AccountAssociationRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.customers.mappers.AccountAssociationMapper;
import com.example.cleaning_service.customers.repositories.AccountAssociationRepository;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class AccountAssociationService {

    private final AccountAssociationRepository accountAssociationRepository;
    private final AccountAssociationMapper accountAssociationMapper;

    public AccountAssociationService(AccountAssociationRepository accountAssociationRepository, AccountAssociationMapper accountAssociationMapper) {
        this.accountAssociationRepository = accountAssociationRepository;
        this.accountAssociationMapper = accountAssociationMapper;
    }

    @Transactional
    protected AccountAssociation saveAccountAssociation(@NotNull AccountAssociationRequest accountAssociationRequest) {
        AccountAssociation accountAssociation =
                accountAssociationMapper.fromAccountAssociationRequestToAccountAssociation(accountAssociationRequest);
        return accountAssociationRepository.save(accountAssociation);
    }

    @Transactional
    public AccountAssociation createAccountAssociation(@NotNull AccountAssociationRequest accountAssociationRequest) {
        return saveAccountAssociation(accountAssociationRequest);
    }

    @Transactional
    protected boolean isExistsAccountAssociationByUser(@NotNull User user) {
        return accountAssociationRepository.existsAccountAssociationByUser(user);
    }

    @Transactional
    protected AccountAssociation getAccountAssociationByCustomer(@NotNull AbstractCustomer customer) {
        return accountAssociationRepository.findByCustomer(customer);
    }

    @Transactional
    public void detachCustomerFromAssociation(@NotNull AbstractCustomer customer) {
        AccountAssociation association = getAccountAssociationByCustomer(customer);
        association.setCustomer(null);
        accountAssociationRepository.save(association);
    }
}
