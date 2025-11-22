package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.authorization.PermissionRole;
import com.codeshare.airline.auth.entities.identity.Permission;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.utils.mappers.PermissionRoleMapper;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.repository.PermissionRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.service.PermissionRoleAssignmentService;
import com.codeshare.airline.common.auth.model.PermissionRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PermissionRoleAssignmentServiceImpl implements PermissionRoleAssignmentService {

    private final PermissionRoleRepository permissionRoleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRoleMapper mapper;

    @Autowired
    public PermissionRoleAssignmentServiceImpl(PermissionRoleRepository permissionRoleRepository, PermissionRepository permissionRepository, RoleRepository roleRepository, PermissionRoleMapper mapper) {
        this.permissionRoleRepository = permissionRoleRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Override
    public PermissionRoleDTO assignPermissionToRole(UUID roleId, UUID permissionId) {

        if (permissionRoleRepository.existsByPermissionIdAndRoleId(permissionId, roleId)) {
            throw new RuntimeException("Permission already assigned to this role");
        }

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));

        PermissionRole entity = PermissionRole.builder()
                .role(role)
                .permission(permission)
                .build();

        PermissionRole saved = permissionRoleRepository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public void removePermissionFromRole(UUID roleId, UUID permissionId) {
        permissionRoleRepository.findByRoleId(roleId).stream()
                .filter(pr -> pr.getPermission().getId().equals(permissionId))
                .findFirst()
                .ifPresent(permissionRoleRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionRoleDTO> getPermissionsByRole(UUID roleId) {
        return mapper.toDTOList(permissionRoleRepository.findByRoleId(roleId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionRoleDTO> getRolesByPermission(UUID permissionId) {
        return mapper.toDTOList(permissionRoleRepository.findByPermissionId(permissionId));
    }
}

