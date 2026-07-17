package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineAlias;


public interface AirlineAliasRepository extends CSMDataBaseRepository<AirlineAlias, Long> {
    boolean existsByAirline_IataCodeAndAliasCode(String iataCode, String aliasCode);
}
