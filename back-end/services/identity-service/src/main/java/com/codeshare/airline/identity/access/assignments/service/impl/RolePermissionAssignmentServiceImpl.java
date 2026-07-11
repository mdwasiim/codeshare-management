package com.codeshare.airline.identity.access.assignments.service.impl;


import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import com.codeshare.airline.identity.access.assignments.entities.RolePermission;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.assignments.repository.GroupRoleRepository;
import com.codeshare.airline.identity.access.assignments.repository.RolePermissionRepository;
import com.codeshare.airline.identity.access.assignments.repository.UserGroupRepository;
import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.identity.access.authorization.repository.PermissionRepository;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import com.codeshare.airline.identity.access.authorization.mappers.PermissionMapper;
import com.codeshare.airline.identity.access.identity.mappers.RoleMapper;
import com.codeshare.airline.identity.access.assignments.mappers.RolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RolePermissionAssignmentServiceImpl implements RolePermissionAssignmentService {

    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionMapper mapper;

    private final GroupRoleRepository groupRoleRepository;
    private final UserGroupRepository userGroupRepository;


    private final RoleMapper tenantRbacRoleMapper;
    private final PermissionMapper permissionMapper;

    // -------------------------------------------------------------------------
    // List permissions BY role
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<RolePermissionDTO> getPermissionsByRole(UUID roleId) {
        return mapper.toDTOList(rolePermissionRepository.findByRoleId(roleId));
    }

    // -------------------------------------------------------------------------
    // List roles BY permission
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<RolePermissionDTO> getRolesByPermission(UUID permissionId) {
        return mapper.toDTOList(rolePermissionRepository.findByPermissionId(permissionId));
    }



    // -------------------------------------------------------------------------
    // Resolve all roles for a user (direct + group inherited)
    // -------------------------------------------------------------------------
    @Override
    public Set<RoleDTO> resolveRoles(UUID userId) {

        Set<Role> roles = new HashSet<>();


        // 2️⃣ Group Roles (UserGroupRole → GroupRole → Role)
        List<UserGroup> userGroups = userGroupRepository.findByUser_Id(userId);

        for (UserGroup ugr : userGroups) {
            UUID groupId = ugr.getGroup().getId();

            List<GroupRole> groupRoles =
                    groupRoleRepository.findByGroup_Id(groupId);

            roles.addAll(groupRoles.stream()
                    .map(GroupRole::getRole)
                    .collect(Collectors.toSet()));
        }

        return tenantRbacRoleMapper.toDTOSet(roles);
    }


    // -------------------------------------------------------------------------
    // Resolve all permissions for a user
    // -------------------------------------------------------------------------
    @Override
    public Set<PermissionDTO> resolvePermissions(UUID userId) {

        // 1️⃣ Get roles for user
        Set<RoleDTO> roleDTOS = resolveRoles(userId);

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
    public Set<String> resolveRoleCodes(UUID userId) {

        return resolveRoles(userId).stream()
                .map(RoleDTO::getCode)   // ✅ better
                .collect(Collectors.toSet());
    }


    // -------------------------------------------------------------------------
    // Convenience: only return permission names
    // -------------------------------------------------------------------------
    @Override
    public Set<String> resolvePermissionCodes(UUID userId) {

        return resolvePermissions(userId).stream()
                .map(PermissionDTO::getCode)   // ✅ MUST
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public List<RolePermissionDTO> replaceRolePermissions(
            UUID roleId,
            List<UUID> permissionIds
    ) {

        UUID tenantId = TenantContextHolder.getTenant().getId();

        Role role = roleRepository.findById(roleId).orElseThrow(() ->new RuntimeException("Role not found: " + roleId) );

        if (!Objects.equals(role.getTenantId(), tenantId)) {
            throw new RuntimeException("Role does not belong to current tenant: " + roleId);
        }

        // delete old
        rolePermissionRepository.deleteByRoleId(roleId);

        // VERY IMPORTANT
        rolePermissionRepository.flush();

        // clear all case
        if (permissionIds == null || permissionIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<RolePermission> entities =
                permissionIds.stream().distinct().map(permissionId -> {
                            Permission permission = permissionRepository.findById(permissionId) .orElseThrow(() ->
                                                    new RuntimeException(
                                                            "Permission not found: "
                                                                    + permissionId
                                                    )
                                            );

                            if (!Objects.equals(permission.getTenantId(), tenantId)) {
                                throw new RuntimeException("Permission does not belong to current tenant: " + permissionId);
                            }

                            return RolePermission.builder()
                                    .tenantId(tenantId)
                                    .role(role)
                                    .permission(permission)
                                    .build();

                        }).collect(Collectors.toList());

        List<RolePermission> saved =
                rolePermissionRepository.saveAll(entities);

        return mapper.toDTOList(saved);
    }


}
