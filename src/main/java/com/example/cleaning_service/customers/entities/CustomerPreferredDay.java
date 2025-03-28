package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.customers.enums.EDay;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "customer_preferred_days", schema = "customer")
public class CustomerPreferredDay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EDay preferredDay;

    public CustomerPreferredDay() {
    }

    public CustomerPreferredDay(EDay preferredDay) {
        this.preferredDay = preferredDay;
    }

    public UUID getId() {
        return id;
    }

    public EDay getPreferredDay() {
        return preferredDay;
    }

    public void setPreferredDay(EDay preferredDay) {
        this.preferredDay = preferredDay;
    }
}
