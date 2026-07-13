package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineContact;


public interface AirlineContactRepository extends CSMDataBaseRepository<AirlineContact, Long> {
    boolean existsByAirline_IataCodeAndContactCode(String iataCode, String contactCode);
}
