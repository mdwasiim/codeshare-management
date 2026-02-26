package com.codeshare.airline.data.airport.terminal.repository;

import com.codeshare.airline.data.airport.terminal.eitities.PassengerTerminal;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface PassengerTerminalRepository
        extends CSMDataBaseRepository<PassengerTerminal, UUID> {

    boolean existsByAirportCodeAndTerminalCode(
            String airportCode,
            String terminalCode
    );

    List<PassengerTerminal> findByAirportId(UUID airportId);
}