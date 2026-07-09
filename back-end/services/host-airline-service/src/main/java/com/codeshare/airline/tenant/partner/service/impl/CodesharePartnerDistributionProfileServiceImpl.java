package com.codeshare.airline.tenant.partner.service.impl;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.tenant.partner.entities.CodesharePartnerDistributionProfile;
import com.codeshare.airline.tenant.partner.mappers.CodesharePartnerDistributionProfileMapper;
import com.codeshare.airline.tenant.partner.repository.CodesharePartnerDistributionProfileRepository;
import com.codeshare.airline.tenant.partner.repository.CodesharePartnerRepository;
import com.codeshare.airline.tenant.partner.service.CodesharePartnerDistributionProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerDistributionProfileServiceImpl implements CodesharePartnerDistributionProfileService {

    private final CodesharePartnerDistributionProfileRepository repository;
    private final CodesharePartnerDistributionProfileMapper mapper;
    private final CodesharePartnerRepository partnerRepository;

    @Override
    public CodesharePartnerDistributionProfileDTO create(CodesharePartnerDistributionProfileDTO dto) {
        CodesharePartnerDistributionProfile entity = mapper.toEntity(dto);
        entity.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerDistributionProfileDTO update(UUID id, CodesharePartnerDistributionProfileDTO dto) {
        CodesharePartnerDistributionProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner distribution profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerDistributionProfileDTO getById(UUID id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner distribution profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerDistributionProfileDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
