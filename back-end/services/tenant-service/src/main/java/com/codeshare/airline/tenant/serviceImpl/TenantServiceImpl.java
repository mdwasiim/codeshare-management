package com.codeshare.airline.tenant.serviceImpl;

import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.service.TenantService;
import com.codeshare.airline.tenant.utils.mappers.TenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantServiceImpl implements TenantService {

    private final TenantRepository repository;
    private final TenantMapper mapper;


    // -------------------------------------------------------------------------
    // CREATE TENANT
    // -------------------------------------------------------------------------
    @Override
    public TenantDTO create(TenantDTO dto) {

        // Unique tenant code validation
        if (repository.existsByCode(dto.getCode())) {
            throw new IllegalStateException("Tenant code already exists: " + dto.getCode());
        }

        Tenant entity = mapper.toEntity(dto);
        Tenant saved = repository.save(entity);

        return mapper.toDTO(saved);
    }


    // -------------------------------------------------------------------------
    // UPDATE TENANT (partial update)
    // -------------------------------------------------------------------------
    @Override
    public TenantDTO update(UUID id, TenantDTO dto) {

        Tenant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found: " + id));

        // If code is changing, ensure new one is unique
        if (dto.getCode() != null && !dto.getCode().equals(entity.getCode())) {
            if (repository.existsByCode(dto.getCode())) {
                throw new IllegalStateException("Tenant code already exists: " + dto.getCode());
            }
        }

        // Let MapStruct apply safe, non-null updates
        mapper.updateEntityFromDto(dto, entity);

        return mapper.toDTO(repository.save(entity));
    }


    // -------------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public TenantDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found: " + id));
    }


    // -------------------------------------------------------------------------
    // GET BY CODE
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public TenantDTO getByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found: " + code));
    }


    // -------------------------------------------------------------------------
    // GET ALL TENANTS
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<TenantDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }


    // -------------------------------------------------------------------------
    // DELETE TENANT
    // -------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tenant not found: " + id);
        }

        // ⚠️ Optional: block deletion if tenant has organizations (recommended)
        // if (organizationRepository.existsByTenantId(id)) {
        //     throw new IllegalStateException("Cannot delete tenant with organizations. Delete child entities first.");
        // }

        repository.deleteById(id);
    }
}
