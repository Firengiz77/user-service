package org.example.userservice.dto;

import org.example.userservice.enums.Permission;

import java.util.List;

public class UserRoleDto {

    private String username;
    private String role;
    private List<Permission> permissions;
}
