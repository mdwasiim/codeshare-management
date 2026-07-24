package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface AirlineRepository extends CSMDataBaseRepository<Airline, Long> {

    Optional<Airline> findByIataCode(String iataCode);

    Optional<Airline> findByIcaoCode(String icaoCode);
}
