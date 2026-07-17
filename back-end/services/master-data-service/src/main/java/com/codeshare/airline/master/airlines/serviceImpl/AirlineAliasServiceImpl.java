package com.codeshare.airline.master.airlines.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineAliasDTO;
import com.codeshare.airline.master.airlines.entities.AirlineAlias;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.mappers.AirlineAliasMapper;
import com.codeshare.airline.master.airlines.repository.AirlineAliasRepository;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airlines.service.AirlineAliasService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AirlineAliasServiceImpl extends BaseServiceImpl<AirlineAlias, AirlineAliasDTO, Long> implements AirlineAliasService {
    private final AirlineCarrierRepository airlineCarrierRepository;

    public AirlineAliasServiceImpl(AirlineAliasRepository repository, AirlineAliasMapper mapper, AirlineCarrierRepository airlineCarrierRepository) {
        super(repository, mapper);
        this.airlineCarrierRepository = airlineCarrierRepository;
    }

    private AirlineCarrier airline(Long id) {
        return id == null ? null : airlineCarrierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    @Override
    public AirlineAliasDTO create(AirlineAliasDTO dto) {
        AirlineAlias entity = mapper.toEntity(dto);
        entity.setAirline(airline(dto.getAirlineId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AirlineAliasDTO update(Long id, AirlineAliasDTO dto) {
        AirlineAlias existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline alias not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setAirline(airline(dto.getAirlineId()));
        return mapper.toDTO(repository.save(existing));
    }
}
