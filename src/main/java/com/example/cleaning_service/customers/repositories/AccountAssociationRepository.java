package com.example.cleaning_service.customers.repositories;

import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.AccountAssociation;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountAssociationRepository extends JpaRepository<AccountAssociation, UUID> {
    List<AccountAssociation> findByCustomer(AbstractCustomer customer);

    Integer countByCustomer(AbstractCustomer customer);

    boolean existsAccountAssociationByUserAndCustomer(@NotNull User user, @NotNull AbstractCustomer customer);

    boolean existsAccountAssociationByUser(@NotNull User user);

    Optional<AccountAssociation> findByUser(User user);

    void deleteByUser(User user);
}
