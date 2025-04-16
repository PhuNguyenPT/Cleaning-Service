package com.example.cleaning_service.security.dtos.auth;

import com.example.cleaning_service.security.dtos.user.PermissionResponse;
import com.example.cleaning_service.security.dtos.user.RoleResponse;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;
import java.util.UUID;

@Getter
@Relation(itemRelation = "user", collectionRelation = "users")
public class AuthResponseProfileModel extends RepresentationModel<AuthResponseProfileModel> {
    private final UUID id;
    private final String username;
    private final Set<RoleResponse> roles;
    private final Set<PermissionResponse> permissions;

    public AuthResponseProfileModel(UUID id, String username, Set<RoleResponse> roles, Set<PermissionResponse> permissions) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.permissions = permissions;
    }
}
