package com.codeshare.airline.master.terminal.serviceImpl;

import com.codeshare.airline.core.dto.master.terminal.PassengerTerminalDTO;
import com.codeshare.airline.master.geography.entities.Airport;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.terminal.entities.PassengerTerminal;
import com.codeshare.airline.master.terminal.repository.PassengerTerminalRepository;
import com.codeshare.airline.master.terminal.service.PassengerTerminalService;
import com.codeshare.airline.master.terminal.mappers.PassengerTerminalMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PassengerTerminalServiceImpl
        extends BaseServiceImpl<PassengerTerminal, PassengerTerminalDTO, UUID>
        implements PassengerTerminalService {

    private final PassengerTerminalRepository repository;
    private final AirportRepository airportRepository;
    private final PassengerTerminalMapper mapper;

    public PassengerTerminalServiceImpl(
            PassengerTerminalRepository repository,
            PassengerTerminalMapper mapper,
            AirportRepository airportRepository) {

        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.airportRepository = airportRepository;
    }

    private Airport getAirport(String code) {
        return airportRepository.findByIataCode(code)
                .orElseThrow(() ->
                        new EntityNotFoundException("Airport not found"));
    }

    @Override
    public PassengerTerminalDTO create(PassengerTerminalDTO dto) {

        // Validate duplicate terminal per airport
        if (repository.existsByAirport_IataCodeAndTerminalCode(
                dto.getAirportCode(),
                dto.getTerminalCode())) {

            throw new IllegalStateException(
                    "Terminal already exists for this airport."
            );
        }

        Airport airport = getAirport(dto.getAirportCode());

        PassengerTerminal entity = mapper.toEntity(dto);
        entity.setAirport(airport);

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public PassengerTerminalDTO update(UUID id, PassengerTerminalDTO dto) {

        PassengerTerminal existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Terminal not found"));

        Airport airport = getAirport(dto.getAirportCode());

        mapper.updateEntityFromDto(dto, existing);
        existing.setAirport(airport);

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public List<PassengerTerminalDTO> getByAirport(UUID airportId) {
        return mapper.toDTOList(
                repository.findByAirportId(airportId)
        );
    }
}
