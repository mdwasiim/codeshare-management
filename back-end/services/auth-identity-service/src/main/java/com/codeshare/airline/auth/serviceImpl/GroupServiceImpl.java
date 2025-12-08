package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.UserGroupRepository;
import com.codeshare.airline.auth.service.GroupService;
import com.codeshare.airline.auth.utils.mappers.GroupMapper;
import com.codeshare.airline.common.auth.identity.model.GroupDTO;
import com.codeshare.airline.common.auth.identity.model.TenantGroupSyncDTO;
import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupMapper mapper;

    // --------------------------------------------------------------------
    // CREATE NEW GROUP
    // --------------------------------------------------------------------
    @Override
    public GroupDTO create(GroupDTO dto) {

        if (dto.getTenantId() == null)
            throw new IllegalArgumentException("tenantId is required");

        if (dto.getName() == null || dto.getName().isBlank())
            throw new IllegalArgumentException("name is required");

        // Check duplicate name inside tenant
        if (groupRepository.existsByNameAndTenantId(dto.getName(), dto.getTenantId()))
            throw new RuntimeException("Group with same name already exists for tenant");

        Group saved = groupRepository.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }


    // --------------------------------------------------------------------
    // UPDATE GROUP DETAILS
    // --------------------------------------------------------------------
    @Override
    public GroupDTO update(UUID id, GroupDTO dto) {

        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + id));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());

        return mapper.toDTO(groupRepository.save(entity));
    }


    // --------------------------------------------------------------------
    // GET GROUP BY ID
    // --------------------------------------------------------------------
    @Override
    public GroupDTO getById(UUID id) {

        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + id));

        return mapper.toDTO(entity);
    }


    // --------------------------------------------------------------------
    // GET GROUPS BY TENANT
    // --------------------------------------------------------------------
    @Override
    public List<GroupDTO> getByTenant(UUID tenantId) {
        return mapper.toDTOList(groupRepository.findByTenantId(tenantId));
    }


    // --------------------------------------------------------------------
    // DELETE GROUP + REMOVE USER-LINKS
    // --------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + id));

        // Remove all user → group mappings
        userGroupRepository.deleteByGroupId(entity.getId());

        groupRepository.delete(entity);
    }


    // ==========================================================================
    // SYNC METHODS (TenantService → AuthService)
    // This updates/creates a group using external TenantService data
    // ==========================================================================
    @Override
    public GroupDTO syncTenantGroup(TenantGroupSyncDTO dto) {

        // Find if group already exists by tenantGroupId
        Group group = groupRepository.findByTenantGroupId(dto.getTenantGroupId())
                .orElse(null);

        if (group == null) {
            // Create new group
            group = new Group();
            group.setTenantGroupId(dto.getTenantGroupId());  // Link to TenantService
        }

        // Update fields from TenantService
        group.setTenantId(dto.getTenantId());
        group.setCode(dto.getCode());
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());

        // If organizationId exists in your entity, add it here.
        // Example: group.setOrganizationId(dto.getOrganizationId());

        return mapper.toDTO(groupRepository.save(group));
    }


    // --------------------------------------------------------------------
    // DELETE GROUP SYNCED FROM TENANT SERVICE
    // --------------------------------------------------------------------
    @Override
    public void deleteByTenantGroupId(UUID tenantGroupId) {

        Group group = groupRepository.findByTenantGroupId(tenantGroupId)
                .orElse(null);

        if (group == null) return; // Safe idempotent delete

        // Remove all user-group mappings
        userGroupRepository.deleteByGroupId(group.getId());

        groupRepository.delete(group);
    }
}
