package com.example.cleaning_service.security.dtos.auth;

import com.example.cleaning_service.security.dtos.user.PermissionResponse;
import com.example.cleaning_service.security.dtos.user.RoleResponse;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;
import java.util.UUID;

@Relation(itemRelation = "user", collectionRelation = "users")
public class AuthResponseProfileModel extends RepresentationModel<AuthResponseProfileModel> {
    private final UUID id;
    private final String username;
    private final RoleResponse role;
    private final Set<PermissionResponse> permissions;

    public AuthResponseProfileModel(UUID id, String username, RoleResponse role, Set<PermissionResponse> permissions) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.permissions = permissions;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public RoleResponse getRole() {
        return role;
    }

    public Set<PermissionResponse> getPermissions() {
        return permissions;
    }
}
