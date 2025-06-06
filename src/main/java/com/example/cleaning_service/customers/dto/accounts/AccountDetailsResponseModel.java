package com.example.cleaning_service.customers.dto.accounts;

import com.example.cleaning_service.customers.enums.EAssociationType;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Getter
public class AccountDetailsResponseModel extends RepresentationModel<AccountDetailsResponseModel> {
    private final UUID id;
    private final String notes;
    private final Boolean isPrimary;
    private final EAssociationType associationType;

    public AccountDetailsResponseModel(UUID id, String notes, Boolean isPrimary, EAssociationType associationType) {
        this.id = id;
        this.notes = notes;
        this.isPrimary = isPrimary;
        this.associationType = associationType;
    }

    @Override
    public @NonNull String toString() {
        return "AccountDetailsResponseModel{" +
                "notes='" + notes + '\'' +
                ", isPrimary=" + isPrimary +
                ", associationType=" + associationType +
                '}';
    }
}
