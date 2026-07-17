package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.terminal.entities.AirportTerminal;

import java.util.Optional;

public interface AirportTerminalRepository extends CSMDataBaseRepository<AirportTerminal, Long> {

    Optional<AirportTerminal> findByTerminalCode(String terminalCode);

    boolean existsByTerminalCode(String terminalCode);
}
