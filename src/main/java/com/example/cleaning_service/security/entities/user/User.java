package com.example.cleaning_service.security.entities.user;

import com.example.cleaning_service.audit.Auditable;
import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.entities.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "users", schema = "security")
public class User extends Auditable implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;


    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(name = "user_roles",
            schema = "security",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(name = "user_permissions",
            schema = "security",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    public User() {
    }

    public User(String username, String password, Role role, Set<Permission> permissions) {
        this.username = username;
        this.password = password;
        addRole(role);
        this.permissions = permissions;
        this.isEnabled = true;
        this.isAccountNonExpired = true;
        this.isCredentialsNonExpired = true;
        this.isAccountNonLocked = true;
    }

    public User(String username, String password, Set<Role> roles, Set<Permission> permissions) {
        this.username = username;
        this.password = password;
        addAllRoles(roles);
        this.permissions = permissions;
        this.isEnabled = true;
        this.isAccountNonExpired = true;
        this.isCredentialsNonExpired = true;
        this.isAccountNonLocked = true;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addAllRoles(Set<Role> roles) {
        this.roles.addAll(roles);
        log.info("Roles after added: {}", roles);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role removeRole) {
        this.roles.removeIf(userRole -> userRole.getName().equals(removeRole.getName()));

    }

    public void removeAllRoles(Set<Role> removeRoles) {
        removeRoles.forEach(this::removeRole);
    }

    public void addPermissions(Set<Permission> permissions) {
        this.permissions.addAll(permissions);
    }

    public void removePermission(Permission removePermission) {
        this.permissions.removeIf(permission -> permission.getName().equals(removePermission.getName()));
    }

    public void removePermissions(Set<Permission> removePermissions) {
        removePermissions.forEach(this::removePermission);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>(permissions);
        Set<GrantedAuthority> roleAuthorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toSet());
        authorities.addAll(roleAuthorities);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                '}';
    }
}