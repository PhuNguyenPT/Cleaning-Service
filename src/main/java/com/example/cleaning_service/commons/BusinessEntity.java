package com.example.cleaning_service.commons;

import com.example.cleaning_service.audit.Auditable;
import com.example.cleaning_service.customers.enums.ECountryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@MappedSuperclass
public class BusinessEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String address;

    @Pattern(
            regexp = "^\\+?[1-9]\\d{1,14}$",
            message = "Invalid international phone number format. " +
                    "Expected format: optional '+' followed by 2 to 15 digits. " +
                    "Examples: +1234567890, 1234567890."
    )
    private String phone;

    @Email(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Invalid email format. " +
                    "Expected format: local-part@domain.extension. " +
                    "- Local part: letters, numbers, dots (.), underscores (_), percent (%), plus (+), or hyphens (-). " +
                    "- Domain: letters, numbers, dots (.) and hyphens (-). " +
                    "- Extension: 2 to 6 letters (e.g., .com, .net, .org). " +
                    "Examples: user@example.com, john.doe@company.org."
    )
    @Column(unique = true)
    private String email;

    private String city;
    private String state;
    private String zip;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECountryType country;

    private String notes;

    public BusinessEntity() {
    }

    public BusinessEntity(String name, String address, String phone, String email, String city, String state, String zip, ECountryType country, String notes) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ECountryType getCountry() {
        return country;
    }

    public void setCountry(ECountryType country) {
        this.country = country;
    }
}