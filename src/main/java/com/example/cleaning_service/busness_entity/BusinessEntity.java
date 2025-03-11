package com.example.cleaning_service.busness_entity;

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

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$",
            message = "Invalid international phone number format")
    private String phone;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Please provide a valid email address")
    private String email;

    private String city;
    private String state;
    private String zip;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECountryType countryType;

    private String notes;

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

    public ECountryType getCountryType() {
        return countryType;
    }

    public void setCountryType(ECountryType countryType) {
        this.countryType = countryType;
    }
}