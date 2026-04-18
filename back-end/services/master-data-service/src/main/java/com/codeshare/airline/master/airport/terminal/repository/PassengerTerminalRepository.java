package com.codeshare.airline.master.airport.terminal.repository;

import com.codeshare.airline.master.airport.terminal.eitities.PassengerTerminal;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

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