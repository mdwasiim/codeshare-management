package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.State;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface StateRepository extends CSMDataBaseRepository<State, Long> {
    Optional<State> findByStateCodeAndCountry_Iso2Code(String stateCode, String iso2Code);
}