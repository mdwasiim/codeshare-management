package com.codeshare.airline.identity.access.assignments.service.impl;

import com.codeshare.airline.core.dto.tenant.GroupRoleDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.assignments.repository.GroupRoleRepository;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.access.assignments.service.GroupRoleAssignmentService;
import com.codeshare.airline.identity.access.assignments.mappers.GroupRoleMapper;
import com.codeshare.airline.identity.access.identity.mappers.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupRoleAssignmentServiceImpl implements GroupRoleAssignmentService {

    private final GroupRoleRepository groupRoleRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final GroupRoleMapper groupRoleMapper;
    private final RoleMapper roleMapper;

    @Override
    public GroupRoleDTO assignRoleToGroup(UUID groupId, UUID roleId) {

        if (groupRoleRepository.existsByGroup_IdAndRole_Id(groupId, roleId)) {
            throw new RuntimeException("Role already assigned to this group");
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        GroupRole entity = GroupRole.builder()
                .group(group)
                .role(role)
                .build();

        return groupRoleMapper.toDTO(groupRoleRepository.save(entity));
    }

    @Override
    public void removeRoleFromGroup(UUID groupId, UUID roleId) {
        groupRoleRepository.findByGroup_Id(groupId).stream()
                .filter(x -> x.getRole().getId().equals(roleId))
                .findFirst()
                .ifPresent(groupRoleRepository::delete);
    }

    @Override
    public List<RoleDTO> getRolesByGroup(UUID groupId) {
        List<GroupRole> groupRoles = groupRoleRepository.findByGroup_Id(groupId);

        return groupRoles.stream()
                .map(groupRole -> roleMapper.toDTO(groupRole.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRoleDTO> getGroupsByRole(UUID roleId) {
        return groupRoleMapper.toDTOList(groupRoleRepository.findByRole_Id(roleId));
    }

    @Override
    @Transactional
    public List<GroupRoleDTO> replaceGroupRoles(
            UUID groupId,
            List<UUID> roleIds
    ) {
        UUID tenantId = TenantContextHolder.getTenant().getId();

        // =====================================================
        // VALIDATE GROUP
        // =====================================================
        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Group not found: " + groupId
                        )
                );

        if (!Objects.equals(group.getTenantId(), tenantId)) {
            throw new RuntimeException("Group does not belong to current tenant: " + groupId);
        }

        // =====================================================
        // REMOVE EXISTING ROLES
        // =====================================================
        groupRoleRepository
                .deleteByGroup_Id(groupId);
        // IMPORTANT
        groupRoleRepository.flush();
        // =====================================================
        // NO ROLES SELECTED
        // =====================================================
        if (roleIds == null || roleIds.isEmpty()) {

            return List.of();
        }

        // =====================================================
        // LOAD ROLES
        // =====================================================
        List<Role> roles =
                roleRepository.findAllById(roleIds);

        if (roles.size() != roleIds.stream().distinct().count()) {
            throw new RuntimeException("One or more roles were not found");
        }

        roles.forEach(role -> {
            if (!Objects.equals(role.getTenantId(), tenantId)) {
                throw new RuntimeException("Role does not belong to current tenant: " + role.getId());
            }
        });

        // =====================================================
        // CREATE NEW MAPPINGS
        // =====================================================
        List<GroupRole> entities =
                roles.stream()

                        .map(role -> GroupRole.builder()

                                .group(group)

                                .role(role)

                                .tenantId(group.getTenantId())

                                .build())

                        .collect(Collectors.toList());

        // =====================================================
        // SAVE
        // =====================================================
        List<GroupRole> groupRoles = groupRoleRepository.saveAll(entities);

        // =====================================================
        // RETURN DTO
        // =====================================================
        return groupRoleMapper.toDTOList(groupRoles);
    }
}
