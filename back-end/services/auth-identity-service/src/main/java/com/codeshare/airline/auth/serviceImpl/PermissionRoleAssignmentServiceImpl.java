package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.rbac.Permission;
import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.entities.rbac.RolePermission;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.repository.PermissionRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.service.PermissionRoleAssignmentService;
import com.codeshare.airline.auth.utils.mappers.PermissionRoleMapper;
import com.codeshare.airline.common.auth.identity.model.PermissionRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionRoleAssignmentServiceImpl implements PermissionRoleAssignmentService {

    private final PermissionRoleRepository permissionRoleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRoleMapper mapper;

    // -------------------------------------------------------------------------
    // Assign a SINGLE permission to a role
    // -------------------------------------------------------------------------
    @Override
    public PermissionRoleDTO assignPermissionToRole(UUID roleId, UUID permissionId) {

        // Check duplicate assignment
        if (permissionRoleRepository.findByRoleIdAndPermissionId(roleId, permissionId).isPresent()) {
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

        return mapper.toDTO(permissionRoleRepository.save(entity));
    }

    // -------------------------------------------------------------------------
    // Assign MULTIPLE permissions to a role
    // -------------------------------------------------------------------------
    @Override
    public List<PermissionRoleDTO> assignPermissionsToRole(UUID roleId, List<UUID> permissionIds) {

        // Validate role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        // Process each permission
        List<RolePermission> entities = permissionIds.stream()
                .distinct() // prevent duplicates in input
                .map(permissionId -> {

                    // Skip if already assigned
                    boolean exists = permissionRoleRepository
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
                .filter(entity -> entity != null) // remove already existing ones
                .collect(Collectors.toList());

        return mapper.toDTOList(permissionRoleRepository.saveAll(entities));
    }

    // -------------------------------------------------------------------------
    // Remove a SINGLE permission from a role
    // -------------------------------------------------------------------------
    @Override
    public void removePermissionFromRole(UUID roleId, UUID permissionId) {

        RolePermission mapping = permissionRoleRepository
                .findByRoleIdAndPermissionId(roleId, permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not assigned to this role"));

        permissionRoleRepository.delete(mapping);
    }

    // -------------------------------------------------------------------------
    // List permissions BY role
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<PermissionRoleDTO> getPermissionsByRole(UUID roleId) {
        return mapper.toDTOList(permissionRoleRepository.findByRoleId(roleId));
    }

    // -------------------------------------------------------------------------
    // List roles BY permission
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<PermissionRoleDTO> getRolesByPermission(UUID permissionId) {
        return mapper.toDTOList(permissionRoleRepository.findByPermissionId(permissionId));
    }
}
