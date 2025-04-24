package com.example.cleaning_service.customers.services.impl;

import com.example.cleaning_service.customers.assemblers.accounts.AccountDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AccountModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AdminAccountDetailsModelAssembler;
import com.example.cleaning_service.customers.assemblers.accounts.AdminAccountModelAssembler;
import com.example.cleaning_service.customers.controllers.AccountController;
import com.example.cleaning_service.customers.dto.accounts.AccountDetailsResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountRequest;
import com.example.cleaning_service.customers.dto.accounts.AccountResponseModel;
import com.example.cleaning_service.customers.dto.accounts.AccountUpdateRequest;
import com.example.cleaning_service.customers.entities.*;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.customers.repositories.AccountRepository;
import com.example.cleaning_service.customers.services.AccountService;
import com.example.cleaning_service.customers.services.OrganizationDetailsService;
import com.example.cleaning_service.security.controllers.AuthController;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Service class that manages associations between users and customer accounts.
 * <p>
 * This service performs the following operations:
 * 1. Provides methods for creating, retrieving, counting, and modifying account associations.
 * 2. Acts as the main interface for managing relationships between users and customer entities.
 * 3. Ensures referential integrity when detaching associations.
 */
@Service
class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final AccountDetailsModelAssembler accountDetailsModelAssembler;
    private final OrganizationDetailsService organizationDetailsService;
    private final AccountModelAssembler accountModelAssembler;
    private final PagedResourcesAssembler<Account> pagedResourcesAssembler;
    private final AdminAccountDetailsModelAssembler adminAccountDetailsModelAssembler;
    private final AdminAccountModelAssembler adminAccountModelAssembler;

    AccountServiceImpl(AccountRepository accountRepository,
                       AccountDetailsModelAssembler accountDetailsModelAssembler,
                       OrganizationDetailsService organizationDetailsService,
                       AccountModelAssembler accountModelAssembler,
                       AdminAccountDetailsModelAssembler adminAccountDetailsModelAssembler,
                       AdminAccountModelAssembler adminAccountModelAssembler,
                       PagedResourcesAssembler<Account> pagedResourcesAssembler) {
        this.accountRepository = accountRepository;
        this.accountDetailsModelAssembler = accountDetailsModelAssembler;
        this.organizationDetailsService = organizationDetailsService;
        this.accountModelAssembler = accountModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.adminAccountDetailsModelAssembler = adminAccountDetailsModelAssembler;
        this.adminAccountModelAssembler = adminAccountModelAssembler;
    }

    @Transactional
    Account findById(UUID id) {
        log.info("Start retrieving account details with id: {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + " not found"));
    }

    @Transactional
    Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account findAccountWithCustomerByUser(User user) {
        log.info("Attempting to find account for {}", user);
        return accountRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("User " + user.getUsername() + "'s account not found"));
    }

    @Override
    @Transactional
    public void checkAccountReferenceCustomer(User user) {
        Account account = accountRepository.findByUser(user)
                .orElse(null);
        if (account == null) {return;}
        log.info("Attempting to check account reference a customer: {}", account);
        if (account.getCustomer() != null) {
            throw new EntityExistsException("Account with ID: " + account.getId() +
                    " already references a Customer with ID: " + account.getCustomer().getId());
        }
        log.info("Account with ID: {} does not reference a Customer", account.getId());
    }

    /**
     * Creates a new account association from a request.
     * <p>
     * This method performs the following actions:
     * 1. Maps the provided request to an `AccountAssociation` entity.
     * 2. Persists the entity in the database.
     * 3. Returns the saved entity.
     *
     * @param accountRequest The request containing association details.
     * @return The created and persisted account association entity.
     */
    @Override
    @Transactional
    public Account handleCustomerCreation(@Valid AccountRequest accountRequest) {
        Account account = accountRepository.findByUser(accountRequest.user())
                .orElse(new Account());
        if (accountRequest.user() != null) {
            account.setUser(accountRequest.user());
        }
        if (accountRequest.customer() != null) {
            account.setCustomer(accountRequest.customer());
        }
        if (accountRequest.isPrimary() != null) {
            account.setPrimary(accountRequest.isPrimary());
        }
        if (accountRequest.notes() != null) {
            account.setNotes(accountRequest.notes());
        }
        if (accountRequest.associationType() != null) {
            account.setAssociationType(accountRequest.associationType());
        }
        return saveAccount(account);
    }

    @Override
    @Transactional
    public void detachCustomerFromAccount(@NotNull AbstractCustomer abstractCustomer) {
        List<Account> accounts = findAllByCustomer(abstractCustomer);
        accounts.forEach(accountAssociation -> accountAssociation.setCustomer(null));
        accountRepository.saveAll(accounts);
    }

    List<Account> findAllByCustomer(AbstractCustomer abstractCustomer) {
        return accountRepository.findByCustomer(abstractCustomer);
    }

    @Override
    @Transactional
    public AccountResponseModel getAccountResponseModelById(User user) {
        log.info("Start retrieving account details for {}", user);
        Account account = findAccountWithCustomerByUser(user);
        log.info("Retrieved account entity {}", account);

        AccountResponseModel accountResponseModel = accountModelAssembler.toModel(account);
        log.info("Retrieved account model {}", accountResponseModel);

        if (account.getCustomer() != null) {
            Link customerLink = organizationDetailsService.getLinkByIOrganization((IOrganization) account.getCustomer());
            log.info("Retrieved customer link {}", customerLink);
            accountResponseModel.add(customerLink);
        }

        Link userProfileLink = linkTo(methodOn(AuthController.class).getAuthenticatedUser(user)).withRel("profile");
        log.info("Retrieved user profile link {}", userProfileLink);

        accountResponseModel.add(userProfileLink);

        return accountResponseModel;
    }

    @Override
    @Transactional
    public AccountDetailsResponseModel getAccountDetailsResponseModelById(UUID id, User user) {
        Account account = findById(id);
        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User " + user.getUsername()  + " is not authorized to access this account with id " + id);
        }
        AccountDetailsResponseModel accountDetailsResponseModel = accountDetailsModelAssembler.toModel(account);
        log.info("Retrieved account details model {}", accountDetailsResponseModel);

        Link accountDefaultLink = linkTo(methodOn(AccountController.class).getAccountByUser(user)).withRel("me");
        log.info("Retrieved account link {}", accountDefaultLink);
        accountDetailsResponseModel.add(accountDefaultLink);

        return accountDetailsResponseModel;
    }

    @Transactional
    public boolean isRepresentativeAssociationType(Account account) {
        return account.getAssociationType().equals(EAssociationType.REPRESENTATIVE);
    }

    @Override
    @Transactional
    public PagedModel<AccountResponseModel> getAdminAccountDetailsPageModelByPageable(Pageable pageable) {
        log.info("Attempting to get account page by pageable {}", pageable);
        Page<Account> accountPage = accountRepository.findAll(pageable);

        log.info("Retrieved account page {}", accountPage);
        PagedModel<AccountResponseModel> accountDetailsResponseModelPagedModel = pagedResourcesAssembler.toModel(
                accountPage, adminAccountModelAssembler
        );
        log.info("Retrieved account page model {}", accountDetailsResponseModelPagedModel);
        Map<UUID, Link> uuidLinkMap = new HashMap<>();
        organizationDetailsService.addLinksForIOrganization(
                uuidLinkMap,
                accountPage,
                accountDetailsResponseModelPagedModel,
                Account::getId,
                AccountResponseModel::getId,
                Account::getIOrganization,
                organizationDetailsService::getAdminCustomerLinkByIOrganization,
                AccountResponseModel::addSingleLink
        );

        return accountDetailsResponseModelPagedModel;
    }

    @Override
    @Transactional
    public AccountDetailsResponseModel getAdminAccountDetailsResponseModelById(UUID id) {
        log.info("Attempting to get account details by id {}", id);
        Account account = findById(id);
        AccountDetailsResponseModel accountDetailsResponseModel = adminAccountDetailsModelAssembler.toModel(account);
        log.info("Retrieved admin finding: account details model {}", accountDetailsResponseModel);
        return accountDetailsResponseModel;
    }

    @Override
    @Transactional
    public AccountDetailsResponseModel patchAccountDetailsById(UUID id, AccountUpdateRequest accountUpdateRequest) {
        log.info("Attempting to update account details by id {}", id);
        Account account = findById(id);
        log.info("Retrieved account details for patch {}", account);
        Account patchedAccount = patchAccountFields(account, accountUpdateRequest);
        log.info("Patched account details {}", patchedAccount);
        AccountDetailsResponseModel accountDetailsResponseModel = accountDetailsModelAssembler.toModel(patchedAccount);
        log.info("Patched account details model {}", accountDetailsResponseModel);
        return accountDetailsResponseModel;
    }

    @Transactional
    Account patchAccountFields(Account account, AccountUpdateRequest accountUpdateRequest) {
        if (account == null || accountUpdateRequest == null) {
            log.warn("Attempting to patch account fields with account: {}, \n " +
                    "accountUpdateRequest: {}", account, accountUpdateRequest);
            return null;
        }
        if (accountUpdateRequest.notes() != null)  {
            account.setNotes(accountUpdateRequest.notes());
        }
        if (accountUpdateRequest.isPrimary() != null) {
            account.setPrimary(accountUpdateRequest.isPrimary());
        }
        if (accountUpdateRequest.eAssociationType() != null) {
            account.setAssociationType(accountUpdateRequest.eAssociationType());
        }
        return saveAccount(account);
    }
}
