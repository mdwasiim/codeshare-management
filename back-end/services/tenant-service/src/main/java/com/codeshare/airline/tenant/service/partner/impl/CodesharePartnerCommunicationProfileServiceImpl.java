package com.codeshare.airline.tenant.service.partner.impl;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerCommunicationProfile;
import com.codeshare.airline.tenant.mappers.partner.CodesharePartnerCommunicationProfileMapper;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerCommunicationProfileRepository;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerCommunicationProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerCommunicationProfileServiceImpl implements CodesharePartnerCommunicationProfileService {

    private final CodesharePartnerCommunicationProfileRepository repository;
    private final CodesharePartnerCommunicationProfileMapper mapper;
    private final CodesharePartnerRepository partnerRepository;

    @Override
    public CodesharePartnerCommunicationProfileDTO create(CodesharePartnerCommunicationProfileDTO dto) {
        CodesharePartnerCommunicationProfile entity = mapper.toEntity(dto);
        entity.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerCommunicationProfileDTO update(UUID id, CodesharePartnerCommunicationProfileDTO dto) {
        CodesharePartnerCommunicationProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner communication profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partnerRepository.findById(dto.getPartnerId()).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found")));
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerCommunicationProfileDTO getById(UUID id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner communication profile not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerCommunicationProfileDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
