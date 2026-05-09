package com.codeshare.airline.identity.service.serviceImpl;

import com.codeshare.airline.core.dto.tenant.GroupRoleDTO;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.GroupRole;
import com.codeshare.airline.identity.entities.Role;
import com.codeshare.airline.identity.repository.GroupRepository;
import com.codeshare.airline.identity.repository.GroupRoleRepository;
import com.codeshare.airline.identity.repository.RoleRepository;
import com.codeshare.airline.identity.service.GroupRoleAssignmentService;
import com.codeshare.airline.identity.utils.mappers.GroupRoleMapper;
import com.codeshare.airline.identity.utils.mappers.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        // =====================================================
        // CREATE NEW MAPPINGS
        // =====================================================
        List<GroupRole> entities =
                roles.stream()

                        .map(role -> GroupRole.builder()

                                .group(group)

                                .role(role)

                                .tenant(group.getTenant())

                                .build())

                        .collect(Collectors.toList());

        // =====================================================
        // SAVE
        // =====================================================
        List<GroupRole> groupRoles = groupRoleRepository.saveAll(entities);

        // =====================================================
        // RETURN DTO
        // =====================================================
        return groupRoles.stream().map(groupRoleMapper::toDTO).collect(Collectors.toList());
    }
}
