package com.codeshare.airline.master.geography.serviceImpl;

import com.codeshare.airline.core.dto.master.georegion.CityDTO;
import com.codeshare.airline.master.geography.entities.City;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.entities.State;
import com.codeshare.airline.master.geography.repository.CityRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import com.codeshare.airline.master.geography.repository.StateRepository;
import com.codeshare.airline.master.geography.service.CityService;
import com.codeshare.airline.master.geography.mappers.CityMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CityServiceImpl
        extends BaseServiceImpl<City, CityDTO, UUID>
        implements CityService {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(CityRepository cityRepository, CityMapper mapper,
                           StateRepository stateRepository, CountryRepository countryRepository) {
        super(cityRepository, mapper);
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    private State getState(UUID stateId) {
        return stateRepository.findById(stateId)
                .orElseThrow(() -> new EntityNotFoundException("State not found"));
    }

    private Country getCountry(UUID countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
    }

    private void validateStateCountry(State state, Country country) {
        if (state != null && !state.getCountry().getId().equals(country.getId())) {
            throw new IllegalStateException("City state must belong to the selected country.");
        }
    }

    @Override
    public CityDTO create(CityDTO dto) {

        Country country = getCountry(dto.getCountryId());

        State state = null;

        if (dto.getStateId() != null) {
            state = getState(dto.getStateId());
        }

        validateStateCountry(state, country);

        City city = mapper.toEntity(dto);
        city.setCountry(country);
        city.setState(state);

        return mapper.toDTO(repository.save(city));
    }

    @Override
    public CityDTO update(UUID id, CityDTO dto) {

        City existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("City not found"));

        Country country = getCountry(dto.getCountryId());
        State state = dto.getStateId() == null ? null : getState(dto.getStateId());

        validateStateCountry(state, country);

        mapper.updateEntityFromDto(dto, existing);
        existing.setCountry(country);
        existing.setState(state);

        return mapper.toDTO(repository.save(existing));
    }


    @Override
    public List<CityDTO> getByCountry(UUID countryId) {
        return mapper.toDTOList(cityRepository.findByCountryId(countryId));
    }

    @Override
    public List<CityDTO> getByState(UUID stateId) {
        return mapper.toDTOList(cityRepository.findByStateId(stateId));
    }

    @Override
    public Page<CityDTO> search(String keyword, Pageable pageable) {
        return cityRepository.search(keyword, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public List<CityDTO> autocomplete(String keyword) {
        return mapper.toDTOList(cityRepository.autocomplete(keyword));
    }
}
