package com.example.cleaning_service.security.entities.user;

import com.example.cleaning_service.audit.Auditable;
import com.example.cleaning_service.security.entities.permission.Permission;
import com.example.cleaning_service.security.entities.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "security")
public class User extends Auditable implements UserDetails {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;

    @Setter
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            schema = "security",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Setter
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
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
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void removeAllRoles(Set<Role> roles) {
        this.roles.removeAll(roles);
    }

    public void addPermissions(Set<Permission> permissions) {
        this.permissions.addAll(permissions);
    }

    public void removePermissions(Set<Permission> permissions) {
        this.permissions.removeAll(permissions);
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
        Set<GrantedAuthority> roleAuthorities = roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toSet());
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
                ", password='[PROTECTED]'" +
                ", isEnabled=" + isEnabled +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", roles=" + roles +
                ", permissions=" + permissions +
                '}';
    }
}