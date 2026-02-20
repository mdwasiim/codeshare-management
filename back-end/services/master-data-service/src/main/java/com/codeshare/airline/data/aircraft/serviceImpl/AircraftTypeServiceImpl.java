package com.codeshare.airline.data.aircraft.serviceImpl;

import com.codeshare.airline.core.dto.aircraft.AircraftTypeDTO;
import com.codeshare.airline.data.aircraft.eitities.AircraftType;
import com.codeshare.airline.data.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.data.aircraft.service.AircraftTypeService;
import com.codeshare.airline.data.aircraft.utils.mappers.AircraftTypeMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AircraftTypeServiceImpl
        extends BaseServiceImpl<AircraftType, AircraftTypeDTO, UUID>
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
        return repository.findByModelCode(model.toUpperCase())
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft not found"));
    }
}