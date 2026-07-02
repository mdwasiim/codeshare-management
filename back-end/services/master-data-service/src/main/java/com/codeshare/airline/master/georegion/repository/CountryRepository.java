package com.codeshare.airline.master.georegion.repository;

import com.codeshare.airline.master.georegion.entities.Country;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository
        extends CSMDataBaseRepository<Country, UUID> {

    Optional<Country> findByIso3Code(String iso3Code);

    Optional<Country> findByIso2Code(String iso2Code);
}
