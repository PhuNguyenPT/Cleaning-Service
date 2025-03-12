package com.example.cleaning_service.customers.mappers;

import com.example.cleaning_service.customers.dto.AccountAssociationRequest;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import org.springframework.stereotype.Component;

@Component
public class AccountAssociationMapper {

    public AccountAssociation fromAccountAssociationRequestToAccountAssociation(AccountAssociationRequest accountAssociationRequest) {
        return new AccountAssociation(
                accountAssociationRequest.user(),
                accountAssociationRequest.customer()
        );
    }
}
