package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.auth.utils.mappers.GroupMapper;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.service.GroupService;
import com.codeshare.airline.common.auth.model.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper mapper;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, GroupMapper mapper) {
        this.groupRepository = groupRepository;
        this.mapper = mapper;
    }

    @Override
    public GroupDTO create(GroupDTO dto) {
        if (dto.getTenantId() == null) {
            throw new IllegalArgumentException("tenantId is required");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }

        if (groupRepository.existsByNameAndTenantId(dto.getName(), dto.getTenantId())) {
            throw new RuntimeException("Group with same name already exists for tenant");
        }

        Group entity = mapper.toEntity(dto);
        Group saved = groupRepository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public GroupDTO update(UUID id, GroupDTO dto) {
        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found: " + id));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        // Do not allow tenantId change via update for safety
        return mapper.toDTO(groupRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDTO getById(UUID id) {
        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found: " + id));
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> getByTenant(UUID tenantId) {
        return mapper.toDTOList(groupRepository.findByTenantId(tenantId));
    }

    @Override
    public void delete(UUID id) {
        Group entity = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found: " + id));
        groupRepository.delete(entity);
    }
}
