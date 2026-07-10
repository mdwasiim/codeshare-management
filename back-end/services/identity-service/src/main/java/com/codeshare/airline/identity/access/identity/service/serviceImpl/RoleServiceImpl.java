package com.codeshare.airline.identity.access.identity.service.serviceImpl;

import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.mappers.RoleMapper;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.access.identity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repo;
    private final RoleMapper mapper;

    @Override
    public RoleDTO create(RoleDTO dto) {
        UUID tenantId = TenantContextHolder.getTenant().getId();
        if (repo.existsByNameAndTenantId(dto.getName(), tenantId)) {
            throw new RuntimeException("Role already exists for this ingestion");
        }

        Role entity = mapper.toEntity(dto);
        entity.setTenantId(tenantId);
        return mapper.toDTO(repo.save(entity));
    }

    @Override
    public RoleDTO update(UUID id, RoleDTO dto) {
        Role entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());

        return mapper.toDTO(repo.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Role not found: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getById(UUID id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));
    }

    public List<RoleDTO> getAllByTenant(UUID tenantId) {
        return mapper.toDTOList(repo.findByTenantId(tenantId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return mapper.toDTOList(repo.findByTenantId(TenantContextHolder.getTenant().getId()));
    }
}
