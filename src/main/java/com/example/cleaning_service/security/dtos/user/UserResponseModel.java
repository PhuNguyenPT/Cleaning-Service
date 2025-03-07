package com.example.cleaning_service.security.dtos.user;

import com.example.cleaning_service.security.entities.role.ERole;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Relation(itemRelation = "user", collectionRelation = "users")
public class UserResponseModel extends RepresentationModel<UserResponseModel> {
    private Long id;
    private String username;
    private ERole role;
    private Set<PermissionResponse> permissions;

    public UserResponseModel(Long id, String username, ERole role, Set<PermissionResponse> permissions) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public Set<PermissionResponse> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionResponse> permissions) {
        this.permissions = permissions;
    }
}

