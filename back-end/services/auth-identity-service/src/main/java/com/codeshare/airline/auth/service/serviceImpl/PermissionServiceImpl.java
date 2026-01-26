package com.codeshare.airline.auth.service.serviceImpl;

import com.codeshare.airline.auth.entities.Permission;
import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.exceptions.CSMBusinessException;
import com.codeshare.airline.core.exceptions.CSMErrorCodes;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.service.PermissionService;
import com.codeshare.airline.auth.utils.mappers.PermissionMapper;
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

    // ================================================================
    // CREATE PERMISSION
    // ================================================================
    @Override
    public PermissionDTO create(PermissionDTO dto) {

        if (dto.getTenantId() == null)
            throw new CSMBusinessException(CSMErrorCodes.VALIDATION_ERROR, "tenantId is required");

        if (dto.getDomain() == null || dto.getAction() == null)
            throw new CSMBusinessException(CSMErrorCodes.VALIDATION_ERROR, "domain and action are required");

        String code = dto.getDomain() + ":" + dto.getAction();

        // --- Prevent duplicates inside same tenant ---
        if (repo.existsByTenantIdAndCode(dto.getTenantId(), code))
            throw new CSMBusinessException(
                    CSMErrorCodes.DUPLICATE_ENTITY,
                    "Permission already exists for tenant: " + code
            );

        dto.setCode(code);  // enforce domain:action convention

        Permission entity = mapper.toEntity(dto);

        Permission saved = repo.save(entity);
        log.info("✔ Permission created: {}", saved.getCode());

        return mapper.toDTO(saved);
    }

    // ================================================================
    // UPDATE PERMISSION (name + description ONLY)
    // ================================================================
    @Override
    public PermissionDTO update(UUID id, PermissionDTO dto) {

        Permission entity = repo.findById(id)
                .orElseThrow(() -> new CSMBusinessException(
                        CSMErrorCodes.DATA_NOT_FOUND, "Permission not found: " + id));

        // IMPORTANT: domain, action, code MUST NEVER change
        if (dto.getDomain() != null || dto.getAction() != null || dto.getCode() != null) {
            log.warn("⚠ Ignored domain/action/code change for permission {} — such updates are not allowed.", id);
        }

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getActive() != null) entity.setActive(dto.getActive());

        Permission updated = repo.save(entity);

        log.info("✔ Permission updated: {}", updated.getCode());

        return mapper.toDTO(updated);
    }

    // ================================================================
    // GET PERMISSION BY ID
    // ================================================================
    @Override
    @Transactional(readOnly = true)
    public PermissionDTO getById(UUID id) {

        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CSMBusinessException(
                        CSMErrorCodes.DATA_NOT_FOUND, "Permission not found: " + id));
    }

    // ================================================================
    // GET ALL PERMISSIONS BY TENANT
    // ================================================================
    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> getByTenant(UUID tenantId) {

        List<Permission> list = repo.findByTenantId(tenantId);

        if (list.isEmpty()) {
            log.warn("⚠ No permissions found for tenant {}", tenantId);
        }

        return mapper.toDTOList(list);
    }

    // ================================================================
    // DELETE PERMISSION (Soft delete by CSMDataAbstractEntity)
    // ================================================================
    @Override
    public void delete(UUID id) {

        Permission entity = repo.findById(id)
                .orElseThrow(() -> new CSMBusinessException(
                        CSMErrorCodes.DATA_NOT_FOUND, "Permission not found: " + id));

        repo.delete(entity);

        log.info("✔ Permission deleted: {}", entity.getCode());
    }
}
