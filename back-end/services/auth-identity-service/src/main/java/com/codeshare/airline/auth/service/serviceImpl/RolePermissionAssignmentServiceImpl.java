package com.codeshare.airline.auth.service.serviceImpl;


import com.codeshare.airline.auth.model.entities.*;
import com.codeshare.airline.auth.repository.*;
import com.codeshare.airline.auth.service.RolePermissionAssignmentService;
import com.codeshare.airline.auth.utils.mappers.PermissionMapper;
import com.codeshare.airline.auth.utils.mappers.RoleMapper;
import com.codeshare.airline.auth.utils.mappers.RolePermissionMapper;
import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;
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
    // Assign a SINGLE permission to a role
    // -------------------------------------------------------------------------
    @Override
    public RolePermissionDTO assignPermissionToRole(UUID roleId, UUID permissionId) {

        // Check duplicate assignment
        if (rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId).isPresent()) {
            throw new RuntimeException("Permission already assigned to role");
        }

        // Fetch role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        // Fetch permission
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));

        // Create mapping
        RolePermission entity = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        return mapper.toDTO(rolePermissionRepository.save(entity));
    }

    // -------------------------------------------------------------------------
    // Assign MULTIPLE permissions to a role
    // -------------------------------------------------------------------------
    @Override
    public List<RolePermissionDTO> assignPermissionsToRole(UUID roleId, List<UUID> permissionIds) {

        // Validate role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        // Process each permission
        List<RolePermission> entities = permissionIds.stream()
                .distinct() // prevent duplicates in input
                .map(permissionId -> {

                    // Skip if already assigned
                    boolean exists = rolePermissionRepository
                            .findByRoleIdAndPermissionId(roleId, permissionId)
                            .isPresent();

                    if (exists) return null;

                    Permission permission = permissionRepository.findById(permissionId)
                            .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));

                    return RolePermission.builder()
                            .role(role)
                            .permission(permission)
                            .build();
                })
                .filter(Objects::nonNull) // remove already existing ones
                .collect(Collectors.toList());

        return mapper.toDTOList(rolePermissionRepository.saveAll(entities));
    }

    // -------------------------------------------------------------------------
    // Remove a SINGLE permission from a role
    // -------------------------------------------------------------------------
    @Override
    public void removePermissionFromRole(UUID roleId, UUID permissionId) {

        RolePermission mapping = rolePermissionRepository
                .findByRoleIdAndPermissionId(roleId, permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not assigned to this role"));

        rolePermissionRepository.delete(mapping);
    }

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
    public Set<String> resolveRoleNames(UUID userId) {

        return resolveRoles(userId).stream()
                .map(RoleDTO::getName)
                .collect(Collectors.toSet());
    }


    // -------------------------------------------------------------------------
    // Convenience: only return permission names
    // -------------------------------------------------------------------------
    @Override
    public Set<String> resolvePermissionsNames(UUID userId) {

        return resolvePermissions(userId).stream()
                .map(PermissionDTO::getName)
                .collect(Collectors.toSet());
    }
}
