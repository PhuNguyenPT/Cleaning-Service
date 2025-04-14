package com.example.cleaning_service.customers.services;

import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.UUID;

public interface AccountService {
    AccountResponseModel getAccountResponseModelById(User user);
    AccountDetailsResponseModel getAccountDetailsResponseModelById(UUID id, User user);
    PagedModel<AccountResponseModel> getAdminAccountDetailsPageModelByPageable(Pageable pageable);
    AccountDetailsResponseModel getAdminAccountDetailsResponseModelById(UUID id);
    AccountDetailsResponseModel patchAccountDetailsById(UUID id, AccountUpdateRequest accountUpdateRequest);

    Account findAccountWithCustomerByUser(User user);
    void checkAccountReferenceCustomer(User user);
    Account handleCustomerCreation(AccountRequest accountRequest);
    void detachCustomerFromAccount(AbstractCustomer abstractCustomer);
    boolean isRepresentativeAssociationType(Account account);
}
