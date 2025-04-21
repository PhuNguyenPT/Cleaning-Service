package com.example.cleaning_service.providers.entities;

import com.example.cleaning_service.audit.Auditable;
import com.example.cleaning_service.security.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "provider_accounts", schema = "provider")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderAccount extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    private Boolean active = false;

    @Override
    public String toString() {
        return "ProviderAccount{" +
                "id=" + id +
                ", provider=" + provider +
                ", user=" + user +
                ", active=" + active +
                '}';
    }
}
