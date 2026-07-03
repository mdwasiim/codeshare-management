package com.codeshare.airline.master.airlines.codesharepartner.serviceImpl;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.master.airlines.entities.CodesharePartner;
import com.codeshare.airline.master.airlines.repository.CodesharePartnerRepository;
import com.codeshare.airline.master.airlines.codesharepartner.entities.CodesharePartnerDistributionProfile;
import com.codeshare.airline.master.airlines.codesharepartner.mappers.CodesharePartnerDistributionProfileMapper;
import com.codeshare.airline.master.airlines.codesharepartner.repository.CodesharePartnerDistributionProfileRepository;
import com.codeshare.airline.master.airlines.codesharepartner.service.CodesharePartnerDistributionProfileService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CodesharePartnerDistributionProfileServiceImpl extends BaseServiceImpl<CodesharePartnerDistributionProfile, CodesharePartnerDistributionProfileDTO, UUID> implements CodesharePartnerDistributionProfileService {
    private final CodesharePartnerRepository codesharePartnerRepository;

    public CodesharePartnerDistributionProfileServiceImpl(CodesharePartnerDistributionProfileRepository repository,
                                                          CodesharePartnerDistributionProfileMapper mapper,
                                                          CodesharePartnerRepository codesharePartnerRepository) {
        super(repository, mapper);
        this.codesharePartnerRepository = codesharePartnerRepository;
    }

    private CodesharePartner partner(UUID id) {
        return id == null ? null : codesharePartnerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
    }

    @Override
    public CodesharePartnerDistributionProfileDTO create(CodesharePartnerDistributionProfileDTO dto) {
        CodesharePartnerDistributionProfile entity = mapper.toEntity(dto);
        entity.setPartner(partner(dto.getPartnerId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerDistributionProfileDTO update(UUID id, CodesharePartnerDistributionProfileDTO dto) {
        CodesharePartnerDistributionProfile existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner distribution profile not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setPartner(partner(dto.getPartnerId()));
        return mapper.toDTO(repository.save(existing));
    }
}
