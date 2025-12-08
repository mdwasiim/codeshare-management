package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.auth.entities.rbac.GroupRole;
import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.GroupRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.service.GroupRoleAssignmentService;
import com.codeshare.airline.auth.utils.mappers.GroupRoleMapper;
import com.codeshare.airline.common.auth.identity.model.GroupRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupRoleAssignmentServiceImpl implements GroupRoleAssignmentService {

    private final GroupRoleRepository repo;
    private final GroupRepository groupRepo;
    private final RoleRepository roleRepo;
    private final GroupRoleMapper mapper;

    @Override
    public GroupRoleDTO assignRoleToGroup(UUID groupId, UUID roleId) {

        if (repo.existsByGroup_IdAndRole_Id(groupId, roleId)) {
            throw new RuntimeException("Role already assigned to this group");
        }

        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        GroupRole entity = GroupRole.builder()
                .group(group)
                .role(role)
                .build();

        return mapper.toDTO(repo.save(entity));
    }

    @Override
    public void removeRoleFromGroup(UUID groupId, UUID roleId) {
        repo.findByGroup_Id(groupId).stream()
                .filter(x -> x.getRole().getId().equals(roleId))
                .findFirst()
                .ifPresent(repo::delete);
    }

    @Override
    public List<GroupRoleDTO> getRolesByGroup(UUID groupId) {
        return mapper.toDTOList(repo.findByGroup_Id(groupId));
    }

    @Override
    public List<GroupRoleDTO> getGroupsByRole(UUID roleId) {
        return mapper.toDTOList(repo.findByGroup_Id(roleId));
    }
}
