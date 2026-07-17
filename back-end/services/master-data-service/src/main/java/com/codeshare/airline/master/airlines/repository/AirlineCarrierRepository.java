package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;

import java.util.Optional;

public interface AirlineCarrierRepository extends CSMDataBaseRepository<AirlineCarrier, Long> {

    Optional<AirlineCarrier> findByIataCode(String iataCode);

    Optional<AirlineCarrier> findByIcaoCode(String icaoCode);
}
