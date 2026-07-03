package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;

import java.util.Optional;
import java.util.UUID;

public interface AirlineCarrierRepository extends CSMDataBaseRepository<AirlineCarrier, UUID> {

    Optional<AirlineCarrier> findByIataCode(String iataCode);

    Optional<AirlineCarrier> findByIcaoCode(String icaoCode);
}
