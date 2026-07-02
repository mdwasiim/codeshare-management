package com.codeshare.airline.master.airline.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airline.entities.AirlineContact;

import java.util.UUID;

public interface AirlineContactRepository extends CSMDataBaseRepository<AirlineContact, UUID> {
}
