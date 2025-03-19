package com.example.cleaning_service.customers.entities;

import com.example.cleaning_service.audit.Auditable;
import com.example.cleaning_service.customers.enums.EAssociationType;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "account", schema = "sale")
public class Account extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

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

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AbstractCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(AbstractCustomer customer) {
        this.customer = customer;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public EAssociationType getAssociationType() {
        return associationType;
    }

    public void setAssociationType(EAssociationType associationType) {
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