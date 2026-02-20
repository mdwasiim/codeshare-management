package com.codeshare.airline.data.core.repository;

import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AirlineCarrierRepository
        extends CSMDataBaseRepository<AirlineCarrier, UUID> {

    Optional<AirlineCarrier> findByIataCode(String iataCode);

    Optional<AirlineCarrier> findByIcaoCode(String icaoCode);

    List<AirlineCarrier> findByCountryId(UUID countryId);

    Optional<AirlineCarrier> findByAirlineCode(String qr);
}