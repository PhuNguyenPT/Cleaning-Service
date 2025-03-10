package com.example.cleaning_service.security.dtos.auth;

import com.example.cleaning_service.security.dtos.user.PermissionResponse;
import com.example.cleaning_service.security.dtos.user.RoleResponse;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;
import java.util.UUID;

@Relation(itemRelation = "user", collectionRelation = "users")
public class AuthResponseProfileModel extends RepresentationModel<AuthResponseProfileModel> {
    private UUID id;
    private String username;
    private RoleResponse role;
    private Set<PermissionResponse> permissions;

    public AuthResponseProfileModel(UUID id, String username, RoleResponse role, Set<PermissionResponse> permissions) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.permissions = permissions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleResponse getRole() {
        return role;
    }

    public void setRole(RoleResponse role) {
        this.role = role;
    }

    public Set<PermissionResponse> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionResponse> permissions) {
        this.permissions = permissions;
    }
}
