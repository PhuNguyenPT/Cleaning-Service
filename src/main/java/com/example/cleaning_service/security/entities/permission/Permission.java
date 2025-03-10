package com.example.cleaning_service.security.entities.permission;

import com.example.cleaning_service.audit.Auditable;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Entity
@Table(name = "permissions")
public class Permission extends Auditable implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EPermission name;

    public Permission() {}

    public Permission(EPermission name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public EPermission getName() {
        return name;
    }

    public void setName(EPermission name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name.toString();
    }
}
