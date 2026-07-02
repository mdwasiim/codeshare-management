package com.codeshare.airline.master.codesharepartner.serviceImpl;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.master.airline.entities.CodesharePartner;
import com.codeshare.airline.master.airline.repository.CodesharePartnerRepository;
import com.codeshare.airline.master.codesharepartner.entities.CodesharePartnerCommunicationProfile;
import com.codeshare.airline.master.codesharepartner.mappers.CodesharePartnerCommunicationProfileMapper;
import com.codeshare.airline.master.codesharepartner.repository.CodesharePartnerCommunicationProfileRepository;
import com.codeshare.airline.master.codesharepartner.service.CodesharePartnerCommunicationProfileService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CodesharePartnerCommunicationProfileServiceImpl extends BaseServiceImpl<CodesharePartnerCommunicationProfile, CodesharePartnerCommunicationProfileDTO, UUID> implements CodesharePartnerCommunicationProfileService {
    private final CodesharePartnerRepository codesharePartnerRepository;

    public CodesharePartnerCommunicationProfileServiceImpl(CodesharePartnerCommunicationProfileRepository repository,
                                                           CodesharePartnerCommunicationProfileMapper mapper,
                                                           CodesharePartnerRepository codesharePartnerRepository) {
        super(repository, mapper);
        this.codesharePartnerRepository = codesharePartnerRepository;
    }

    private CodesharePartner partner(UUID id) {
        return id == null ? null : codesharePartnerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
    }

    @Override
    public CodesharePartnerCommunicationProfileDTO create(CodesharePartnerCommunicationProfileDTO dto) {
        CodesharePartnerCommunicationProfile entity = mapper.toEntity(dto);
        entity.setPartner(partner(dto.getPartnerId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerCommunicationProfileDTO update(UUID id, CodesharePartnerCommunicationProfileDTO dto) {
        CodesharePartnerCommunicationProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner communication profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partner(dto.getPartnerId()));
        return mapper.toDTO(repository.save(existing));
    }
}
