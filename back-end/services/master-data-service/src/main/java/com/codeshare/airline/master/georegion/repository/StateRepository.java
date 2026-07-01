package com.codeshare.airline.master.georegion.repository;

import com.codeshare.airline.master.georegion.eitities.State;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface StateRepository extends CSMDataBaseRepository<State, UUID> {
    Optional<State> findByStateCodeAndCountry_Iso2Code(String stateCode, String iso2Code);
}