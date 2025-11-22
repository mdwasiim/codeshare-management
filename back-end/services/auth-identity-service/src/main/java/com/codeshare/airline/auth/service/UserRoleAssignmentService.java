package com.codeshare.airline.auth.service;

import com.codeshare.airline.common.auth.model.UserRoleDTO;

import java.util.List;
import java.util.UUID;

public interface UserRoleAssignmentService {

    UserRoleDTO assignRoleToUser(UUID userId, UUID roleId);

    void removeRoleFromUser(UUID userId, UUID roleId);

    List<UserRoleDTO> getRolesByUser(UUID userId);

    List<UserRoleDTO> getUsersByRole(UUID roleId);
}

