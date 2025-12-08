package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.rbac.*;
import com.codeshare.airline.auth.repository.*;
import com.codeshare.airline.auth.service.RolePermissionResolverService;
import com.codeshare.airline.auth.utils.mappers.PermissionMapper;
import com.codeshare.airline.auth.utils.mappers.RoleMapper;
import com.codeshare.airline.common.auth.identity.model.PermissionDTO;
import com.codeshare.airline.common.auth.identity.model.RoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolePermissionResolverServiceServiceImpl implements RolePermissionResolverService {

    private final GroupRoleRepository groupRoleRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final PermissionRepository permissionRepository;

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;


    // -------------------------------------------------------------------------
    // Resolve all roles for a user (direct + group inherited)
    // -------------------------------------------------------------------------
    @Override
    public Set<RoleDTO> resolveRoles(UUID userId, UUID tenantId) {

        Set<Role> roles = new HashSet<>();

        // 1️⃣ Direct User Roles
        List<UserRole> directRoles = userRoleRepository.findByUserIdAndTenantId(userId, tenantId);
        roles.addAll(directRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet()));

        // 2️⃣ Group Roles (UserGroupRole → GroupRole → Role)
        List<UserGroupRole> userGroups = userGroupRoleRepository.findByUserIdAndTenantId(userId, tenantId);

        for (UserGroupRole ugr : userGroups) {
            UUID groupId = ugr.getGroup().getId();

            List<GroupRole> groupRoles =
                    groupRoleRepository.findByGroup_IdAndTenantId(groupId, tenantId);

            roles.addAll(groupRoles.stream()
                    .map(GroupRole::getRole)
                    .collect(Collectors.toSet()));
        }

        return roleMapper.toDTOSet(roles);
    }


    // -------------------------------------------------------------------------
    // Resolve all permissions for a user
    // -------------------------------------------------------------------------
    @Override
    public Set<PermissionDTO> resolvePermissions(UUID userId, UUID tenantId) {

        // 1️⃣ Get roles for user
        Set<RoleDTO> roleDTOS = resolveRoles(userId, tenantId);

        if (roleDTOS.isEmpty())
            return Collections.emptySet();

        // Extract role IDs
        Set<UUID> roleIds = roleDTOS.stream()
                .map(RoleDTO::getId)
                .collect(Collectors.toSet());

        // 2️⃣ Get permissions mapped to those roles
        List<Permission> permissions =
                permissionRepository.findByRoleIds(roleIds);

        return permissionMapper.toDTOSet(new HashSet<>(permissions));
    }


    // -------------------------------------------------------------------------
    // Convenience: only return role names
    // -------------------------------------------------------------------------
    @Override
    public Set<String> resolveRoleNames(UUID userId, UUID tenantId) {

        return resolveRoles(userId, tenantId).stream()
                .map(RoleDTO::getName)
                .collect(Collectors.toSet());
    }


    // -------------------------------------------------------------------------
    // Convenience: only return permission names
    // -------------------------------------------------------------------------
    @Override
    public Set<String> resolvePermissionsNames(UUID userId, UUID tenantId) {

        return resolvePermissions(userId, tenantId).stream()
                .map(PermissionDTO::getName)
                .collect(Collectors.toSet());
    }
}
