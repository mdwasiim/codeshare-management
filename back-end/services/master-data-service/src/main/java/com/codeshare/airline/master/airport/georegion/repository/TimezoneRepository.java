package com.codeshare.airline.master.airport.georegion.repository;

import com.codeshare.airline.master.airport.georegion.eitities.Timezone;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface TimezoneRepository
        extends CSMDataBaseRepository<Timezone, UUID> {
    Optional<Timezone> findByTzIdentifier(String s);
}