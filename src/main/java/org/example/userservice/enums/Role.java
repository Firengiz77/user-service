package org.example.userservice.enums;

import java.util.Set;

public enum Role {

    USER(Set.of(Permission.UPDATE_PROFILE,Permission.LOGIN_AS_USER)),
    ADMIN(Set.of(Permission.UPDATE_PROFILE, Permission.MANAGE_USERS,Permission.LOGIN_AS_ADMIN));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

}
