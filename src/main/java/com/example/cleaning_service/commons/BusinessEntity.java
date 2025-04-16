package com.example.cleaning_service.commons;

import com.example.cleaning_service.audit.Auditable;
import com.example.cleaning_service.customers.enums.ECountryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BusinessEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "address")
    protected String address;

    @Column(name = "phone")
    protected String phone;

    @Column(name = "email")
    protected String email;

    @Column(name = "city")
    protected String city;

    @Column(name = "state")
    protected String state;

    @Column(name = "zip")
    protected String zip;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    protected ECountryType country;

    @Column(name = "notes")
    protected String notes;

    public BusinessEntity(String name, String address, String phone, String email, String city, String state,
                          String zip, ECountryType country, String notes) {
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

    @Override
    public String toString() {
        return "BusinessEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", country=" + country +
                ", notes='" + notes + '\'' +
                '}';
    }
}