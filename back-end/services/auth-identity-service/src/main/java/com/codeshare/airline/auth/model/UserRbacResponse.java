package com.codeshare.airline.auth.model;


import com.codeshare.airline.common.auth.model.MenuDTO;
import com.codeshare.airline.common.auth.model.PermissionDTO;
import com.codeshare.airline.common.auth.model.RoleDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class UserRbacResponse {
    private UUID userId;
    private Set<RoleDTO> roles;
    private Set<PermissionDTO> permissions;
    private List<MenuDTO> menus;
}
