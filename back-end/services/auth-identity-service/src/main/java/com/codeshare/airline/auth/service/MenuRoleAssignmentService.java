package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.identity.model.MenuRoleDTO;

import java.util.List;
import java.util.UUID;

public interface MenuRoleAssignmentService {

    MenuRoleDTO assignMenuToRole(UUID roleId, UUID menuId);

    void removeMenuFromRole(UUID roleId, UUID menuId);

    List<MenuRoleDTO> getMenusByRole(UUID roleId);

    List<MenuRoleDTO> getRolesByMenu(UUID menuId);
}

