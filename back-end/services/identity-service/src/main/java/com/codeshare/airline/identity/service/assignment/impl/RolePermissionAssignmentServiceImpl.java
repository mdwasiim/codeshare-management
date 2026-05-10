package com.codeshare.airline.identity.service.assignment.impl;


import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;
import com.codeshare.airline.identity.authentication.domain.TenantContextHolder;
import com.codeshare.airline.identity.entities.*;
import com.codeshare.airline.identity.repository.*;
import com.codeshare.airline.identity.service.assignment.RolePermissionAssignmentService;
import com.codeshare.airline.identity.service.TenantService;
import com.codeshare.airline.identity.utils.mappers.PermissionMapper;
import com.codeshare.airline.identity.utils.mappers.RoleMapper;
import com.codeshare.airline.identity.utils.mappers.RolePermissionMapper;
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

    private final TenantService tenantService;
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

        Tenant tenant = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());

        Role role = roleRepository.findById(roleId).orElseThrow(() ->new RuntimeException("Role not found: " + roleId) );

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

                            return RolePermission.builder()
                                    .tenant(tenant)
                                    .role(role)
                                    .permission(permission)
                                    .build();

                        }).collect(Collectors.toList());

        List<RolePermission> saved =
                rolePermissionRepository.saveAll(entities);

        return mapper.toDTOList(saved);
    }


}
