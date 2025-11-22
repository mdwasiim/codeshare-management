package com.codeshare.airline.tenant.serviceImpl;


import com.codeshare.airline.common.exception.ResourceNotFoundException;
import com.codeshare.airline.common.tenant.model.OrganizationDTO;
import com.codeshare.airline.tenant.entities.Organization;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.repository.OrganizationRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.service.OrganizationService;
import com.codeshare.airline.tenant.utils.mappers.OrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final TenantRepository tenantRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    public OrganizationDTO create(OrganizationDTO organizationDTO) {

        if (organizationDTO.getTenantId() == null) {
            throw new IllegalArgumentException("tenantId is required");
        }

        if (organizationRepository.existsByCodeAndTenantId(organizationDTO.getCode(), organizationDTO.getTenantId())) {
            throw new IllegalArgumentException("Organization code already exists for tenant");
        }

        Tenant tenant = tenantRepository.findById(organizationDTO.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found: " + organizationDTO.getTenantId()));


        Organization entity = organizationMapper.toEntity(organizationDTO);
        entity.setTenant(tenant);

        Organization saved = organizationRepository.save(entity);
        return organizationMapper.toDTO(saved);
    }

    @Override
    public OrganizationDTO update(UUID id, OrganizationDTO organizationDTO) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found: " + id));

        if (organizationDTO.getName() != null){
            organization.setName(organizationDTO.getName());
        }

        if (organizationDTO.getCode() != null) {
            organization.setCode(organizationDTO.getCode());
        }

        if (organizationDTO.getDescription() != null) {
            organization.setDescription(organizationDTO.getDescription());
        }

        organization.setActive(organizationDTO.isEnabled());

        Organization saved = organizationRepository.save(organization);

        return organizationMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationDTO getById(UUID id) {
        Organization entity = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found: " + id));
        return organizationMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationDTO> getAllByTenant(UUID tenantId) {
        return organizationMapper.toDTOList(organizationRepository.findByTenantId(tenantId));
    }


    @Override
    public void delete(UUID id) {
        Organization entity = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found: " + id));
        organizationRepository.delete(entity);
    }
}
