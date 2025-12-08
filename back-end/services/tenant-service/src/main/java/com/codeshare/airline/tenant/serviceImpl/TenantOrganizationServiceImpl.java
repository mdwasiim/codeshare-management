package com.codeshare.airline.tenant.serviceImpl;

import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.TenantOrganization;
import com.codeshare.airline.tenant.repository.TenantOrganizationRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.service.TenantOrganizationService;
import com.codeshare.airline.tenant.utils.mappers.TenantOrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantOrganizationServiceImpl implements TenantOrganizationService {

    private final TenantOrganizationRepository organizationRepository;
    private final TenantRepository tenantRepository;
    private final TenantOrganizationMapper tenantOrganizationMapper;

    // -------------------------------------------------------------------------
    // CREATE TENANT ORGANIZATION
    // -------------------------------------------------------------------------
    @Override
    public TenantOrganizationDTO create(TenantOrganizationDTO dto) {

        if (dto.getTenantId() == null)
            throw new IllegalArgumentException("tenantId is required");

        // Unique code validation per tenant
        if (organizationRepository.existsByCodeAndTenantId(dto.getCode(), dto.getTenantId())) {
            throw new IllegalArgumentException("Organization code already exists for this tenant");
        }

        Tenant tenant = tenantRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found: " + dto.getTenantId()));

        TenantOrganization entity = tenantOrganizationMapper.toEntity(dto);
        entity.setTenant(tenant);

        return tenantOrganizationMapper.toDTO(organizationRepository.save(entity));
    }


    // -------------------------------------------------------------------------
    // UPDATE TENANT ORGANIZATION
    // -------------------------------------------------------------------------
    @Override
    public TenantOrganizationDTO update(UUID id, TenantOrganizationDTO dto) {

        TenantOrganization entity = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TenantOrganization not found: " + id));

        // Validate unique code (if changed)
        if (dto.getCode() != null && !dto.getCode().equals(entity.getCode())) {
            if (organizationRepository.existsByCodeAndTenantId(dto.getCode(), entity.getTenant().getId())) {
                throw new IllegalArgumentException("Organization code already exists for this tenant");
            }
            entity.setCode(dto.getCode());
        }

        // Update partial fields only if provided
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getActive() != null) entity.setActive(dto.getActive());

        return tenantOrganizationMapper.toDTO(organizationRepository.save(entity));
    }


    // -------------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public TenantOrganizationDTO getById(UUID id) {
        return organizationRepository.findById(id)
                .map(tenantOrganizationMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("TenantOrganization not found: " + id));
    }


    // -------------------------------------------------------------------------
    // GET ALL FOR TENANT
    // -------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<TenantOrganizationDTO> getAllByTenant(UUID tenantId) {
        return tenantOrganizationMapper.toDTOList(organizationRepository.findByTenantId(tenantId));
    }


    // -------------------------------------------------------------------------
    // DELETE ORGANIZATION
    // -------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {
        TenantOrganization entity = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TenantOrganization not found: " + id));

        organizationRepository.delete(entity);
    }
}
