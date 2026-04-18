package com.codeshare.airline.master.commercial.codeshare.serviceImpl;

import com.codeshare.airline.core.dto.codeshare.CodeshareEquipmentRuleDTO;
import com.codeshare.airline.master.aircraft.eitities.AircraftType;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareEquipmentRule;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.master.commercial.codeshare.repository.CodeshareEquipmentRuleRepository;
import com.codeshare.airline.master.commercial.codeshare.repository.CodeshareFlightMappingRepository;
import com.codeshare.airline.master.commercial.codeshare.service.CodeshareEquipmentRuleService;
import com.codeshare.airline.master.commercial.codeshare.mappers.CodeshareEquipmentRuleMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodeshareEquipmentRuleServiceImpl
        extends BaseServiceImpl<CodeshareEquipmentRule, CodeshareEquipmentRuleDTO, UUID>
        implements CodeshareEquipmentRuleService {

    private final CodeshareEquipmentRuleRepository repository;
    private final CodeshareFlightMappingRepository flightMappingRepository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final CodeshareEquipmentRuleMapper mapper;

    public CodeshareEquipmentRuleServiceImpl(
            CodeshareEquipmentRuleRepository repository,
            CodeshareEquipmentRuleMapper mapper,
            CodeshareFlightMappingRepository flightMappingRepository,
            AircraftTypeRepository aircraftTypeRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.flightMappingRepository = flightMappingRepository;
        this.aircraftTypeRepository = aircraftTypeRepository;
    }

    private CodeshareFlightMapping getFlightMapping(UUID id) {
        return flightMappingRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Flight mapping not found"));
    }

    private AircraftType getAircraftType(UUID id) {
        return aircraftTypeRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Aircraft type not found"));
    }

    @Override
    public CodeshareEquipmentRuleDTO create(CodeshareEquipmentRuleDTO dto) {

        CodeshareFlightMapping mapping =
                getFlightMapping(dto.getFlightMappingId());

        AircraftType aircraftType =
                getAircraftType(dto.getAircraftTypeId());

        CodeshareEquipmentRule entity = mapper.toEntity(dto);
        entity.setFlightMapping(mapping);
        entity.setAircraftType(aircraftType);

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public List<CodeshareEquipmentRuleDTO> getByFlightMapping(UUID flightMappingId) {
        return mapper.toDTOList(
                repository.findByFlightMappingId(flightMappingId)
        );
    }
}