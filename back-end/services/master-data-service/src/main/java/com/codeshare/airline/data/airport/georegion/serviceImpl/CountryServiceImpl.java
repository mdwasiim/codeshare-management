package com.codeshare.airline.data.airport.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.CountryDTO;
import com.codeshare.airline.data.airport.georegion.eitities.Country;
import com.codeshare.airline.data.airport.georegion.repository.CountryRepository;
import com.codeshare.airline.data.airport.georegion.service.CountryService;
import com.codeshare.airline.data.airport.georegion.utils.mappers.CountryMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CountryServiceImpl  extends BaseServiceImpl<Country, CountryDTO, UUID> implements CountryService {

    public CountryServiceImpl(CountryRepository repository, CountryMapper mapper) {
        super(repository, mapper);
    }
}