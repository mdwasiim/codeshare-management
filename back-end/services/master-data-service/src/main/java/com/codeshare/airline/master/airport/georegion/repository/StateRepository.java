package com.codeshare.airline.master.airport.georegion.repository;

import com.codeshare.airline.master.airport.georegion.eitities.State;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface StateRepository extends CSMDataBaseRepository<State, UUID> {
    Optional<State> findByStateCodeAndCountry_Iso2Code(String stateCode, String iso2Code);
}