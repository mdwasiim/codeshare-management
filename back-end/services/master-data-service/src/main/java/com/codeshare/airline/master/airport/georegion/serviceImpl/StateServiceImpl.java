package com.codeshare.airline.master.airport.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.StateDTO;
import com.codeshare.airline.master.airport.georegion.eitities.Country;
import com.codeshare.airline.master.airport.georegion.eitities.State;
import com.codeshare.airline.master.airport.georegion.repository.CountryRepository;
import com.codeshare.airline.master.airport.georegion.repository.StateRepository;
import com.codeshare.airline.master.airport.georegion.service.StateService;
import com.codeshare.airline.master.airport.georegion.mappers.StateMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StateServiceImpl extends BaseServiceImpl<State, StateDTO, UUID> implements StateService {

    private final CountryRepository countryRepository;

    public StateServiceImpl(StateRepository repository, StateMapper mapper, CountryRepository countryRepository) {
        super(repository, mapper);
        this.countryRepository = countryRepository;
    }

    @Override
    public StateDTO create(StateDTO dto) {

        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));

        State state = mapper.toEntity(dto);
        state.setCountry(country);

        return mapper.toDTO(repository.save(state));
    }

    @Override
    public StateDTO update(UUID id, StateDTO dto) {

        State existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("State not found"));

        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setCountry(country);

        return mapper.toDTO(repository.save(existing));
    }
}