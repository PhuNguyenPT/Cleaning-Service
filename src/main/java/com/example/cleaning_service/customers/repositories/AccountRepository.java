package com.example.cleaning_service.customers.repositories;

import com.example.cleaning_service.customers.entities.AbstractCustomer;
import com.example.cleaning_service.customers.entities.Account;
import com.example.cleaning_service.security.entities.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByCustomer(AbstractCustomer customer);

    @EntityGraph(attributePaths = {"customer"})
    Optional<Account> findByUser(User user);

    void deleteByUser(User user);
}
