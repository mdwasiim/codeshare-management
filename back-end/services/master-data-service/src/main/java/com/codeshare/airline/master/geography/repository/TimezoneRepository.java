package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface TimezoneRepository
        extends CSMDataBaseRepository<Timezone, Long> {
    Optional<Timezone> findByTzIdentifier(String s);
}