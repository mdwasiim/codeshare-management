package com.codeshare.airline.master.terminal.serviceImpl;

import com.codeshare.airline.core.dto.master.terminal.AirportTerminalDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.georegion.entities.Airport;
import com.codeshare.airline.master.georegion.repository.AirportRepository;
import com.codeshare.airline.master.terminal.entities.AirportTerminal;
import com.codeshare.airline.master.terminal.mappers.AirportTerminalMapper;
import com.codeshare.airline.master.terminal.repository.AirportTerminalRepository;
import com.codeshare.airline.master.terminal.service.AirportTerminalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AirportTerminalServiceImpl
        extends BaseServiceImpl<AirportTerminal, AirportTerminalDTO, UUID>
        implements AirportTerminalService {

    private final AirportRepository airportRepository;

    public AirportTerminalServiceImpl(AirportTerminalRepository repository,
                                      AirportTerminalMapper mapper,
                                      AirportRepository airportRepository) {
        super(repository, mapper);
        this.airportRepository = airportRepository;
    }

    private Airport getAirport(UUID airportId) {
        if (airportId == null) {
            return null;
        }

        return airportRepository.findById(airportId)
                .orElseThrow(() -> new EntityNotFoundException("Airport not found"));
    }

    @Override
    public AirportTerminalDTO create(AirportTerminalDTO dto) {
        AirportTerminal entity = mapper.toEntity(dto);
        entity.setAirport(getAirport(dto.getAirportId()));

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AirportTerminalDTO update(UUID id, AirportTerminalDTO dto) {
        AirportTerminal existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airport terminal not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setAirport(getAirport(dto.getAirportId()));

        return mapper.toDTO(repository.save(existing));
    }
}
