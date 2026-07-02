package com.codeshare.airline.master.aircraft.serviceImpl;

import com.codeshare.airline.core.dto.master.aircraft.AircraftFamilyDTO;
import com.codeshare.airline.master.aircraft.entities.AircraftFamily;
import com.codeshare.airline.master.aircraft.entities.AircraftManufacturer;
import com.codeshare.airline.master.aircraft.mappers.AircraftFamilyMapper;
import com.codeshare.airline.master.aircraft.repository.AircraftFamilyRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftManufacturerRepository;
import com.codeshare.airline.master.aircraft.service.AircraftFamilyService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AircraftFamilyServiceImpl
        extends BaseServiceImpl<AircraftFamily, AircraftFamilyDTO, UUID>
        implements AircraftFamilyService {

    private final AircraftFamilyRepository repository;
    private final AircraftManufacturerRepository aircraftManufacturerRepository;

    public AircraftFamilyServiceImpl(
            AircraftFamilyRepository repository,
            AircraftFamilyMapper mapper,
            AircraftManufacturerRepository aircraftManufacturerRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.aircraftManufacturerRepository = aircraftManufacturerRepository;
    }

    @Override
    public AircraftFamilyDTO create(AircraftFamilyDTO dto) {
        AircraftFamily entity = mapper.toEntity(dto);
        entity.setManufacturer(getManufacturer(dto.getManufacturerId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AircraftFamilyDTO update(UUID id, AircraftFamilyDTO dto) {
        AircraftFamily existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft family not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setManufacturer(getManufacturer(dto.getManufacturerId()));
        return mapper.toDTO(repository.save(existing));
    }

    private AircraftManufacturer getManufacturer(UUID id) {
        if (id == null) {
            throw new EntityNotFoundException("Aircraft manufacturer is required");
        }

        return aircraftManufacturerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft manufacturer not found"));
    }
}
