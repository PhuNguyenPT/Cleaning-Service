package com.example.cleaning_service.security.dtos.user;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;
import java.util.UUID;

@Getter
@Relation(itemRelation = "user", collectionRelation = "users")
public class UserResponseModel extends RepresentationModel<UserResponseModel> {
    private final UUID id;
    private final String username;
    private final Set<RoleResponse> roles;
    @Getter
    private final Set<PermissionResponse> permissions;

    public UserResponseModel(UUID id, String username, Set<RoleResponse> roles, Set<PermissionResponse> permissions) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.permissions = permissions;
    }
}

