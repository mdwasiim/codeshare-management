package com.codeshare.airline.identity.access.identity.service.serviceImpl;

import com.codeshare.airline.core.dto.tenant.GroupDTO;
import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.identity.service.GroupService;
import com.codeshare.airline.identity.access.identity.service.TenantService;
import com.codeshare.airline.identity.access.identity.mappers.GroupMapper;
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
    private final GroupMapper mapper;
    private final TenantService tenantService;

    // --------------------------------------------------------------------
    // CREATE NEW GROUP
    // --------------------------------------------------------------------
    @Override
    public GroupDTO create(GroupDTO dto) {

        // 🔥 Fetch tenant entity
        Tenant tenant = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());

        if (dto.getTenantId() == null)
            throw new IllegalArgumentException("tenantId is required");

        if (dto.getName() == null || dto.getName().isBlank())
            throw new IllegalArgumentException("name is required");

        // Check duplicate name inside ingestion
        if (groupRepository.existsByNameAndTenantId(dto.getName(), dto.getTenantId()))
            throw new RuntimeException("Group with same name already exists for ingestion");
        Group entity = mapper.toEntity(dto);
        entity.setTenant(tenant);
        Group saved = groupRepository.save(entity);
        return mapper.toDTO(saved);
    }


    // --------------------------------------------------------------------
    // UPDATE GROUP DETAILS
    // --------------------------------------------------------------------
    @Override
    public GroupDTO update(UUID id, GroupDTO dto) {

        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("Group not found: " + id));

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
                .orElseThrow(() -> new CSMResourceNotFoundException("Group not found: " + id));

        return mapper.toDTO(entity);
    }

    @Override
    public List<GroupDTO> getAll() {
        // 🔥 Fetch tenant entity
        Tenant tenant = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());
        return mapper.toDTOList(groupRepository.findAll());
    }
    // --------------------------------------------------------------------
    // GET GROUPS BY TENANT
    // --------------------------------------------------------------------
    public List<GroupDTO> getByTenant(UUID tenantId) {
        return mapper.toDTOList(groupRepository.findByTenantId(tenantId));
    }


    // --------------------------------------------------------------------
    // DELETE GROUP + REMOVE USER-LINKS
    // --------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new CSMResourceNotFoundException("Group not found: " + id));

        groupRepository.delete(entity);
    }


    // --------------------------------------------------------------------
    // DELETE GROUP SYNCED FROM TENANT SERVICE
    // --------------------------------------------------------------------
    @Override
    public void deleteByTenantGroupId(UUID groupId) {

        Group group = groupRepository.findById(groupId)
                .orElse(null);

        if (group == null) return; // Safe idempotent delete

        groupRepository.delete(group);
    }
}
