package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.identity.Permission;
import com.codeshare.airline.auth.utils.mappers.PermissionMapper;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.service.PermissionService;
import com.codeshare.airline.common.auth.model.PermissionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repo;
    private final PermissionMapper mapper;

    @Autowired
    public PermissionServiceImpl(PermissionRepository repo, PermissionMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public PermissionDTO create(PermissionDTO dto) {

        Permission entity = mapper.toEntity(dto);
        return mapper.toDTO(repo.save(entity));
    }

    @Override
    public PermissionDTO update(UUID id, PermissionDTO dto) {

        Permission entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + id));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        // tenantId should NOT change

        return mapper.toDTO(repo.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDTO getById(UUID id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> getByTenant(UUID tenantId) {
        return mapper.toDTOList(repo.findByTenantId(tenantId));
    }

    @Override
    public void delete(UUID id) {
        Permission entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found: " + id));
        repo.delete(entity);
    }
}
