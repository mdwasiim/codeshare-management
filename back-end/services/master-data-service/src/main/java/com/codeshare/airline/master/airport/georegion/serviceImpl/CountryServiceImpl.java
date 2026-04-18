package com.codeshare.airline.master.airport.georegion.serviceImpl;

import com.codeshare.airline.core.dto.airport.georegion.CountryDTO;
import com.codeshare.airline.master.airport.georegion.eitities.Country;
import com.codeshare.airline.master.airport.georegion.repository.CountryRepository;
import com.codeshare.airline.master.airport.georegion.service.CountryService;
import com.codeshare.airline.master.airport.georegion.mappers.CountryMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CountryServiceImpl  extends BaseServiceImpl<Country, CountryDTO, UUID> implements CountryService {

    public CountryServiceImpl(CountryRepository repository, CountryMapper mapper) {
        super(repository, mapper);
    }
}