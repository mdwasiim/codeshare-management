package com.codeshare.airline.master.airline.serviceImpl;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.master.airline.entities.AirlineCarrier;
import com.codeshare.airline.master.airline.entities.CodesharePartner;
import com.codeshare.airline.master.airline.mappers.CodesharePartnerMapper;
import com.codeshare.airline.master.airline.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airline.repository.CodesharePartnerRepository;
import com.codeshare.airline.master.airline.service.CodesharePartnerService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CodesharePartnerServiceImpl extends BaseServiceImpl<CodesharePartner, CodesharePartnerDTO, UUID> implements CodesharePartnerService {
    private final AirlineCarrierRepository airlineCarrierRepository;

    public CodesharePartnerServiceImpl(CodesharePartnerRepository repository, CodesharePartnerMapper mapper, AirlineCarrierRepository airlineCarrierRepository) {
        super(repository, mapper);
        this.airlineCarrierRepository = airlineCarrierRepository;
    }

    private AirlineCarrier airline(UUID id) {
        return id == null ? null : airlineCarrierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    private void applyRelations(CodesharePartnerDTO dto, CodesharePartner entity) {
        entity.setHomeAirline(airline(dto.getHomeAirlineId()));
        entity.setPartnerAirline(airline(dto.getPartnerAirlineId()));
    }

    @Override
    public CodesharePartnerDTO create(CodesharePartnerDTO dto) {
        CodesharePartner entity = mapper.toEntity(dto);
        applyRelations(dto, entity);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public CodesharePartnerDTO update(UUID id, CodesharePartnerDTO dto) {
        CodesharePartner existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
        mapper.updateEntityFromDto(dto, existing);
        applyRelations(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }
}
