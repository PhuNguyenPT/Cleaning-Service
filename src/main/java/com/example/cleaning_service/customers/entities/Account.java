package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.audit.Auditable;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "customer_account", schema = "sale")
public class Account extends Auditable {
    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private AbstractCustomer customer;

    private String notes;
    private boolean isPrimary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAssociationType associationType;

    public Account() {
    }

    public Account(User user, AbstractCustomer customer, String notes, boolean isPrimary,
                   EAssociationType associationType) {
        this.user = user;
        this.customer = customer;
        this.notes = notes;
        this.isPrimary = isPrimary;
        this.associationType = associationType;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user=" + user +
                ", customer=" + customer +
                ", notes='" + notes + '\'' +
                ", isPrimary=" + isPrimary +
                ", associationType=" + associationType +
                '}';
    }
}