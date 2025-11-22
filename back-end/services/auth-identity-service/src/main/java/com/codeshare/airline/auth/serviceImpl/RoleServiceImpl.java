package com.codeshare.airline.auth.serviceImpl;


import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.auth.utils.mappers.RoleMapper;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.service.RoleService;
import com.codeshare.airline.common.auth.model.RoleDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repo;
    private final RoleMapper mapper;

    public RoleServiceImpl(RoleRepository repo, RoleMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public RoleDTO create(RoleDTO dto) {

        if (repo.existsByNameAndTenantId(dto.getName(), dto.getTenantId())) {
            throw new RuntimeException("Role already exists for this tenant");
        }

        Role entity = mapper.toEntity(dto);
        return mapper.toDTO(repo.save(entity));
    }

    @Override
    public RoleDTO update(UUID id, RoleDTO dto) {
        Role entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        return mapper.toDTO(repo.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new RuntimeException("Role not found");
        repo.deleteById(id);
    }

    @Override
    public RoleDTO getById(UUID id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public List<RoleDTO> getAllByTenant(UUID tenantId) {
        return mapper.toDTOList(repo.findByTenantId(tenantId));
    }

    @Override
    public List<RoleDTO> getAll() {
        return mapper.toDTOList(repo.findAll());
    }
}
