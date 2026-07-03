package com.codeshare.airline.identity.access.identity.service.serviceImpl;

import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import com.codeshare.airline.identity.access.identity.service.RoleService;
import com.codeshare.airline.identity.access.identity.service.TenantService;
import com.codeshare.airline.identity.access.identity.mappers.RoleMapper;
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
    private final TenantRepository tenantRepository;
    private final TenantService tenantService;
    // --------------------------------------------------------------------
    // CREATE NEW ROLE
    // --------------------------------------------------------------------
    @Override
    public RoleDTO create(RoleDTO dto) {
        // 🔥 Fetch tenant entity
        Tenant tenant = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());
        if (dto.getTenantId() == null) {
            throw new IllegalArgumentException("tenantId is required");
        }

        if (repo.existsByNameAndTenantId(dto.getName(), dto.getTenantId())) {
            throw new RuntimeException("Role already exists for this ingestion");
        }

        Role entity = mapper.toEntity(dto);
        entity.setTenant(tenant);
        return mapper.toDTO(repo.save(entity));
    }


    // --------------------------------------------------------------------
    // UPDATE EXISTING ROLE
    // --------------------------------------------------------------------
    @Override
    public RoleDTO update(UUID id, RoleDTO dto) {

        Role entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));

        // Only update fields provided in request
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());

        return mapper.toDTO(repo.save(entity));
    }


    // --------------------------------------------------------------------
    // DELETE ROLE BY ID
    // --------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        if (!repo.existsById(id)) {
            throw new RuntimeException("Role not found: " + id);
        }

        repo.deleteById(id);
    }


    // --------------------------------------------------------------------
    // GET ROLE BY ID
    // --------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public RoleDTO getById(UUID id) {

        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));
    }


    // --------------------------------------------------------------------
    // GET ALL ROLES FOR A TENANT
    // --------------------------------------------------------------------
    public List<RoleDTO> getAllByTenant(UUID tenantId) {

        return mapper.toDTOList(repo.findByTenantId(tenantId));
    }


    // --------------------------------------------------------------------
    // GET ALL ROLES (SUPER ADMIN ONLY)
    // --------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles () {
        Tenant tenant = tenantService.getTenantByTenantCode(TenantContextHolder.getTenant().getTenantCode());
        List<Role> repoByTenant = repo.findByTenant(tenant);
        return mapper.toDTOList(repoByTenant);
    }
}
