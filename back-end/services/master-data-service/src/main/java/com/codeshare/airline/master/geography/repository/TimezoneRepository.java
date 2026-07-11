package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface TimezoneRepository
        extends CSMDataBaseRepository<Timezone, UUID> {
    Optional<Timezone> findByTzIdentifier(String s);
}