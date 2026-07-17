package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface CountryRepository
        extends CSMDataBaseRepository<Country, Long> {

    Optional<Country> findByIso3Code(String iso3Code);

    Optional<Country> findByIso2Code(String iso2Code);
}
