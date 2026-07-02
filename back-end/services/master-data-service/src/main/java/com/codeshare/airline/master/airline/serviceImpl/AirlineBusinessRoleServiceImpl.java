package com.codeshare.airline.master.airline.serviceImpl;

import com.codeshare.airline.core.dto.master.airline.AirlineBusinessRoleDTO;
import com.codeshare.airline.master.airline.entities.AirlineBusinessRole;
import com.codeshare.airline.master.airline.entities.AirlineCarrier;
import com.codeshare.airline.master.airline.mappers.AirlineBusinessRoleMapper;
import com.codeshare.airline.master.airline.repository.AirlineBusinessRoleRepository;
import com.codeshare.airline.master.airline.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airline.service.AirlineBusinessRoleService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AirlineBusinessRoleServiceImpl extends BaseServiceImpl<AirlineBusinessRole, AirlineBusinessRoleDTO, UUID> implements AirlineBusinessRoleService {
    private final AirlineCarrierRepository airlineCarrierRepository;

    public AirlineBusinessRoleServiceImpl(AirlineBusinessRoleRepository repository, AirlineBusinessRoleMapper mapper, AirlineCarrierRepository airlineCarrierRepository) {
        super(repository, mapper);
        this.airlineCarrierRepository = airlineCarrierRepository;
    }

    private AirlineCarrier airline(UUID id) {
        return id == null ? null : airlineCarrierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    @Override
    public AirlineBusinessRoleDTO create(AirlineBusinessRoleDTO dto) {
        AirlineBusinessRole entity = mapper.toEntity(dto);
        entity.setAirline(airline(dto.getAirlineId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AirlineBusinessRoleDTO update(UUID id, AirlineBusinessRoleDTO dto) {
        AirlineBusinessRole existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline business role not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setAirline(airline(dto.getAirlineId()));
        return mapper.toDTO(repository.save(existing));
    }
}
