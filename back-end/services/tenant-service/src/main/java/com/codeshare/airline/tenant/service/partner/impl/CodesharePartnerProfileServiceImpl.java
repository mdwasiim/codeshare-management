package com.codeshare.airline.tenant.service.partner.impl;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;
import com.codeshare.airline.platform.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerProfile;
import com.codeshare.airline.tenant.mappers.partner.CodesharePartnerProfileMapper;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerProfileServiceImpl implements CodesharePartnerProfileService {

    private final CodesharePartnerProfileRepository repository;
    private final CodesharePartnerProfileMapper mapper;
    private final CodesharePartnerRepository partnerRepository;
    private final TenantRepository tenantRepository;

    @Override
    public CodesharePartnerProfileDTO create(CodesharePartnerProfileDTO dto) {
        CodesharePartnerProfile entity = mapper.toEntity(dto);
        entity.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerProfileDTO update(Long id, CodesharePartnerProfileDTO dto) {
        CodesharePartnerProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerProfileDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerProfileDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerProfileDTO> getCurrent(String tenantCode) {
        Long tenantId = resolveTenantId(tenantCode);
        return mapper.toDTOList(repository.findByPartner_TenantIdOrderByPartner_IdAscPriorityAscDisplayOrderAscIdAsc(tenantId));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Long resolveTenantId(String tenantCode) {
        if (tenantCode == null || tenantCode.isBlank()) {
            throw new IllegalArgumentException("Tenant header is required");
        }

        return tenantRepository.findByTenantCode(tenantCode.trim().toUpperCase())
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + tenantCode))
                .getId();
    }
}
