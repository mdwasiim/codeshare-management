package com.codeshare.airline.identity.access.authorization.service.serviceImpl;

import com.codeshare.airline.platform.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.platform.core.exceptions.CSMBusinessException;
import com.codeshare.airline.platform.core.exceptions.CSMErrorCodes;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.identity.access.authorization.mappers.PermissionMapper;
import com.codeshare.airline.identity.access.authorization.repository.PermissionRepository;
import com.codeshare.airline.identity.access.authorization.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repo;
    private final PermissionMapper mapper;

    @Override
    public PermissionDTO create(PermissionDTO dto) {
        UUID tenantId = TenantContextHolder.getTenant().getId();
        if (dto.getDomain() == null || dto.getAction() == null)
            throw new CSMBusinessException(CSMErrorCodes.VALIDATION_ERROR, "domain and action are required");

        String code = dto.getDomain() + ":" + dto.getAction();
        if (repo.existsByTenantIdAndCode(tenantId, code))
            throw new CSMBusinessException(CSMErrorCodes.DUPLICATE_ENTITY, "Permission already exists for ingestion: " + code);

        dto.setCode(code);

        Permission entity = mapper.toEntity(dto);
        entity.setTenantId(tenantId);
        Permission saved = repo.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public PermissionDTO update(UUID id, PermissionDTO dto) {
        Permission entity = repo.findById(id)
                .orElseThrow(() -> new CSMBusinessException(CSMErrorCodes.DATA_NOT_FOUND, "Permission not found: " + id));

        if (dto.getDomain() != null || dto.getAction() != null || dto.getCode() != null) {
            log.warn("Ignored domain/action/code change for permission {}", id);
        }

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        entity.setActive(dto.getActive());

        return mapper.toDTO(repo.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDTO getById(UUID id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CSMBusinessException(CSMErrorCodes.DATA_NOT_FOUND, "Permission not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllTenant() {
        UUID tenantId = TenantContextHolder.getTenant().getId();
        List<Permission> list = repo.findByTenantId(tenantId);
        if (list.isEmpty()) {
            log.warn("No permissions found for ingestion {}", TenantContextHolder.getTenant().getTenantCode());
        }
        return mapper.toDTOList(list);
    }

    @Override
    public void delete(UUID id) {
        Permission entity = repo.findById(id)
                .orElseThrow(() -> new CSMBusinessException(CSMErrorCodes.DATA_NOT_FOUND, "Permission not found: " + id));
        repo.delete(entity);
    }
}
