package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftTypeDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftType;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.master.aircraft.service.AircraftTypeService;
import com.codeshare.airline.master.aircraft.mappers.AircraftTypeMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AircraftTypeServiceImpl
        extends BaseServiceImpl<AircraftType, AircraftTypeDTO, Long>
        implements AircraftTypeService {

    private final AircraftTypeRepository repository;

    public AircraftTypeServiceImpl(
            AircraftTypeRepository repository,
            AircraftTypeMapper mapper) {

        super(repository, mapper);
        this.repository = repository;
    }

    @Override
    public AircraftTypeDTO getByIcao(String icao) {
        return repository.findByIcaoCode(icao.toUpperCase())
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft not found"));
    }

    @Override
    public AircraftTypeDTO getByModel(String model) {
        return repository.findByModel(model.toUpperCase())
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft not found"));
    }
}
