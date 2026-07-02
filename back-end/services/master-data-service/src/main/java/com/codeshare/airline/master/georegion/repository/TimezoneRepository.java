package com.codeshare.airline.master.georegion.repository;

import com.codeshare.airline.master.georegion.entities.Timezone;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface TimezoneRepository
        extends CSMDataBaseRepository<Timezone, UUID> {
    Optional<Timezone> findByTzIdentifier(String s);
}