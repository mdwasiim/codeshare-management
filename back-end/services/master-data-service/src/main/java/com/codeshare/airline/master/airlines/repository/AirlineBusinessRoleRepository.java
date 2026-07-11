package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AirlineBusinessRole;

import java.util.UUID;

public interface AirlineBusinessRoleRepository extends CSMDataBaseRepository<AirlineBusinessRole, UUID> {
    boolean existsByAirline_IataCodeAndRoleCode(String iataCode, String roleCode);
}
