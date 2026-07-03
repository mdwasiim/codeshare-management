package com.codeshare.airline.master.airlines.codesharepartner.serviceImpl;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;
import com.codeshare.airline.master.airlines.entities.CodesharePartner;
import com.codeshare.airline.master.airlines.repository.CodesharePartnerRepository;
import com.codeshare.airline.master.airlines.codesharepartner.entities.CodesharePartnerProfile;
import com.codeshare.airline.master.airlines.codesharepartner.mappers.CodesharePartnerProfileMapper;
import com.codeshare.airline.master.airlines.codesharepartner.repository.CodesharePartnerProfileRepository;
import com.codeshare.airline.master.airlines.codesharepartner.service.CodesharePartnerProfileService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CodesharePartnerProfileServiceImpl extends BaseServiceImpl<CodesharePartnerProfile, CodesharePartnerProfileDTO, UUID> implements CodesharePartnerProfileService {
    private final CodesharePartnerRepository codesharePartnerRepository;

    public CodesharePartnerProfileServiceImpl(CodesharePartnerProfileRepository repository, CodesharePartnerProfileMapper mapper,
                                              CodesharePartnerRepository codesharePartnerRepository) {
        super(repository, mapper);
        this.codesharePartnerRepository = codesharePartnerRepository;
    }

    private CodesharePartner partner(UUID id) {
        return id == null ? null : codesharePartnerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
    }

    @Override
    public CodesharePartnerProfileDTO create(CodesharePartnerProfileDTO dto) {
        CodesharePartnerProfile entity = mapper.toEntity(dto);
        entity.setPartner(partner(dto.getPartnerId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerProfileDTO update(UUID id, CodesharePartnerProfileDTO dto) {
        CodesharePartnerProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partner(dto.getPartnerId()));
        return mapper.toDTO(repository.save(existing));
    }
}
