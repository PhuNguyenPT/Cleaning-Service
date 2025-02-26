package com.example.cleaning_service.customers.repositories;

import com.example.cleaning_service.customers.entities.IndividualCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, Integer> {
}
