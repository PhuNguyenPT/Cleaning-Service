package com.example.cleaning_service.security.roles;

import com.example.cleaning_service.audit.Auditable;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "permissions")
public class Permission extends Auditable implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EPermission name;

    public Permission() {}

    public Permission(EPermission name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
