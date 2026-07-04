package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineContact;

import java.util.UUID;

public interface AirlineContactRepository extends CSMDataBaseRepository<AirlineContact, UUID> {
    boolean existsByAirline_IataCodeAndContactCode(String iataCode, String contactCode);
}
