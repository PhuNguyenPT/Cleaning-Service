package com.example.cleaning_service.security.entities.permission;

import com.example.cleaning_service.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Getter
@Entity
@Table(name = "permissions", schema = "security")
public class Permission extends Auditable implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EPermission name;

    public Permission() {}

    public Permission(EPermission name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name.toString();
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name=" + name +
                ", id=" + id +
                '}';
    }
}
