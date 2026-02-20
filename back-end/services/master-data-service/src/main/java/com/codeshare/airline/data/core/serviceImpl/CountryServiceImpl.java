package com.codeshare.airline.data.core.serviceImpl;

import com.codeshare.airline.core.dto.georegion.CountryDTO;
import com.codeshare.airline.data.core.eitities.Country;
import com.codeshare.airline.data.core.repository.CountryRepository;
import com.codeshare.airline.data.core.service.CountryService;
import com.codeshare.airline.data.core.utils.mappers.CountryMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CountryServiceImpl  extends BaseServiceImpl<Country, CountryDTO, UUID> implements CountryService {

    public CountryServiceImpl(CountryRepository repository, CountryMapper mapper) {
        super(repository, mapper);
    }
}