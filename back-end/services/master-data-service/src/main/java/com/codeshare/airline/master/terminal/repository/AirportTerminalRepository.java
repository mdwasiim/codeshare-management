package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.terminal.entities.AirportTerminal;

import java.util.Optional;
import java.util.UUID;

public interface AirportTerminalRepository extends CSMDataBaseRepository<AirportTerminal, UUID> {

    Optional<AirportTerminal> findByTerminalCode(String terminalCode);

    boolean existsByTerminalCode(String terminalCode);
}
