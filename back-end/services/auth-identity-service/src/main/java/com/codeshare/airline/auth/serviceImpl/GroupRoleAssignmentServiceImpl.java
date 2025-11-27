package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.utils.mappers.GroupRoleMapper;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.GroupRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.service.GroupRoleAssignmentService;
import com.codeshare.airline.common.auth.model.GroupRoleDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GroupRoleAssignmentServiceImpl implements GroupRoleAssignmentService {

    private final GroupRoleRepository repo;
    private final GroupRepository groupRepo;
    private final RoleRepository roleRepo;
    private final GroupRoleMapper mapper;

    public GroupRoleAssignmentServiceImpl(
            GroupRoleRepository repo,
            GroupRepository groupRepo,
            RoleRepository roleRepo,
            GroupRoleMapper mapper
    ) {
        this.repo = repo;
        this.groupRepo = groupRepo;
        this.roleRepo = roleRepo;
        this.mapper = mapper;
    }

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
