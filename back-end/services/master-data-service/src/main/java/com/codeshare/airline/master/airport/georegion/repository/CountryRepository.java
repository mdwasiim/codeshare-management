package com.codeshare.airline.master.airport.georegion.repository;

import com.codeshare.airline.master.airport.georegion.eitities.Country;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository
        extends CSMDataBaseRepository<Country, UUID> {

    Optional<Country> findByIsoCode(String isoCode);

    Optional<Country> findByIso2Code(String iso2Code);
}