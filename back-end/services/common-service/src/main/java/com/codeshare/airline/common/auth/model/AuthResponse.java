package com.codeshare.airline.common.auth.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserDTO userDTO;
    private Set<RoleDTO> roles;
    private Set<PermissionDTO> permissions;
}
