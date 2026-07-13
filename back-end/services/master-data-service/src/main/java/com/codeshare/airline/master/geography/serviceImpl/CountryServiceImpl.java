package com.codeshare.airline.master.geography.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.georegion.CountryDTO;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import com.codeshare.airline.master.geography.service.CountryService;
import com.codeshare.airline.master.geography.mappers.CountryMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class CountryServiceImpl  extends BaseServiceImpl<Country, CountryDTO, Long> implements CountryService {

    public CountryServiceImpl(CountryRepository repository, CountryMapper mapper) {
        super(repository, mapper);
    }
}