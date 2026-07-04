package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineAlias;

import java.util.UUID;

public interface AirlineAliasRepository extends CSMDataBaseRepository<AirlineAlias, UUID> {
    boolean existsByAirline_IataCodeAndAliasCode(String iataCode, String aliasCode);
}
