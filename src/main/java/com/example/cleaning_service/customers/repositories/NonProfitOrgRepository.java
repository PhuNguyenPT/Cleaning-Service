package com.example.cleaning_service.customers.repositories;

import com.example.cleaning_service.customers.entities.NonProfitOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NonProfitOrgRepository extends JpaRepository<NonProfitOrg, UUID> {
}
