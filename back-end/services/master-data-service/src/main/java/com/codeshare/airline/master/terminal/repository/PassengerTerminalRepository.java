package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.master.terminal.entities.PassengerTerminal;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.List;

public interface PassengerTerminalRepository
        extends CSMDataBaseRepository<PassengerTerminal, Long> {

    boolean existsByAirport_IataCodeAndTerminalCode(
            String airportCode,
            String terminalCode
    );

    List<PassengerTerminal> findByAirportId(Long airportId);
}
