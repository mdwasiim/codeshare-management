package com.codeshare.airline.master.airlines.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineContactDTO;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.entities.AirlineContact;
import com.codeshare.airline.master.airlines.mappers.AirlineContactMapper;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airlines.repository.AirlineContactRepository;
import com.codeshare.airline.master.airlines.service.AirlineContactService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AirlineContactServiceImpl extends BaseServiceImpl<AirlineContact, AirlineContactDTO, UUID> implements AirlineContactService {
    private final AirlineCarrierRepository airlineCarrierRepository;
    private final TimezoneRepository timezoneRepository;

    public AirlineContactServiceImpl(AirlineContactRepository repository, AirlineContactMapper mapper,
                                     AirlineCarrierRepository airlineCarrierRepository, TimezoneRepository timezoneRepository) {
        super(repository, mapper);
        this.airlineCarrierRepository = airlineCarrierRepository;
        this.timezoneRepository = timezoneRepository;
    }

    private AirlineCarrier airline(UUID id) {
        return id == null ? null : airlineCarrierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    private Timezone timezone(UUID id) {
        return id == null ? null : timezoneRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Timezone not found"));
    }

    private void applyRelations(AirlineContactDTO dto, AirlineContact entity) {
        entity.setAirline(airline(dto.getAirlineId()));
        entity.setTimeZone(timezone(dto.getTimeZoneId()));
    }

    @Override
    public AirlineContactDTO create(AirlineContactDTO dto) {
        AirlineContact entity = mapper.toEntity(dto);
        applyRelations(dto, entity);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AirlineContactDTO update(UUID id, AirlineContactDTO dto) {
        AirlineContact existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline contact not found"));
        mapper.updateEntityFromDto(dto, existing);
        applyRelations(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }
}
