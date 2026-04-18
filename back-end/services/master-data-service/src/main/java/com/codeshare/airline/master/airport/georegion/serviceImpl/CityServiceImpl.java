package com.codeshare.airline.master.airport.georegion.serviceImpl;

import com.codeshare.airline.dto.airport.georegion.CityDTO;
import com.codeshare.airline.master.airport.georegion.eitities.City;
import com.codeshare.airline.master.airport.georegion.eitities.Country;
import com.codeshare.airline.master.airport.georegion.eitities.State;
import com.codeshare.airline.master.airport.georegion.repository.CityRepository;
import com.codeshare.airline.master.airport.georegion.repository.CountryRepository;
import com.codeshare.airline.master.airport.georegion.repository.StateRepository;
import com.codeshare.airline.master.airport.georegion.service.CityService;
import com.codeshare.airline.master.airport.georegion.utils.mappers.CityMapper;
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

    @Override
    public CityDTO create(CityDTO dto) {

        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));

        State state = null;

        if (dto.getStateId() != null) {
            state = stateRepository.findById(dto.getStateId())
                    .orElseThrow(() -> new EntityNotFoundException("State not found"));
        }

        City city = mapper.toEntity(dto);
        city.setCountry(country);
        city.setState(state);

        return mapper.toDTO(repository.save(city));
    }

    @Override
    public CityDTO update(UUID id, CityDTO dto) {

        City existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("City not found"));

        State state = getState(dto.getStateId());

        mapper.updateEntityFromDto(dto, existing);
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