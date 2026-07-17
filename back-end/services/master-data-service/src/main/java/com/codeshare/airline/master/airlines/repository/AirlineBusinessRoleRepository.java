package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineBusinessRole;


public interface AirlineBusinessRoleRepository extends CSMDataBaseRepository<AirlineBusinessRole, Long> {
    boolean existsByAirline_IataCodeAndRoleCode(String iataCode, String roleCode);
}
