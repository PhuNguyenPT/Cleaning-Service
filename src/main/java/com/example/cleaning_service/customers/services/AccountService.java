package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.security.entities.user.User;

import java.util.UUID;

public interface AccountService {
    Account getAccountDetailsResponseModelById(UUID id, User user);
    Account findById(UUID id);
    Account patchAccountDetailsById(UUID id, AccountUpdateRequest accountUpdateRequest);

    Account findAccountWithCustomerByUser(User user);
    void checkAccountReferenceCustomer(User user);
    Account handleCustomerCreation(AccountRequest accountRequest);
    void detachCustomerFromAccount(AbstractCustomer abstractCustomer);
    boolean isRepresentativeAssociationType(Account account);
}
