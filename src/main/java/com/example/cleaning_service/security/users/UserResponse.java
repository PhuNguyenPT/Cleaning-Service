package com.example.cleaning_service.security.users;

import com.example.cleaning_service.security.roles.ERole;
import com.example.cleaning_service.security.roles.Permission;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

public class UserResponse extends RepresentationModel<UserResponse> {
    private final Long id;
    private final String username;
    private final ERole role;
    private final Set<Permission> permissions;

    public UserResponse(Long id, String username, ERole role, Set<Permission> permissions) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public ERole getRole() {
        return role;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}