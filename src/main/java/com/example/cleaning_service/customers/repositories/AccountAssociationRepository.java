package com.example.cleaning_service.customers.repositories;

import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountAssociationRepository extends JpaRepository<AccountAssociation, UUID> {
    boolean existsAccountAssociationByUser(User user);
    AccountAssociation findByCustomer(AbstractCustomer customer);
}
