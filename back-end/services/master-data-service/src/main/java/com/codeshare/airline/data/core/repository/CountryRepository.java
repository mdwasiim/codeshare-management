package com.codeshare.airline.data.core.repository;

import com.codeshare.airline.data.core.eitities.Country;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository
        extends CSMDataBaseRepository<Country, UUID> {

    Optional<Country> findByIsoCode(String isoCode);

    Optional<Country> findByIso2Code(String iso2Code);
}