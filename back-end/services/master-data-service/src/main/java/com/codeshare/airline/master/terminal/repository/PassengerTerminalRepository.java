package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.master.terminal.entities.PassengerTerminal;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.UUID;

public interface PassengerTerminalRepository
        extends CSMDataBaseRepository<PassengerTerminal, UUID> {

    boolean existsByAirport_IataCodeAndTerminalCode(
            String airportCode,
            String terminalCode
    );

    List<PassengerTerminal> findByAirportId(UUID airportId);
}
