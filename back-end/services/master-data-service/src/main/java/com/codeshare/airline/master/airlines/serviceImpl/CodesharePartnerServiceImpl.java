package com.codeshare.airline.master.airlines.serviceImpl;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.entities.CodesharePartner;
import com.codeshare.airline.master.airlines.mappers.CodesharePartnerMapper;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airlines.repository.CodesharePartnerRepository;
import com.codeshare.airline.master.airlines.service.CodesharePartnerService;
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
