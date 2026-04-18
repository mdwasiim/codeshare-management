package com.codeshare.airline.master.airport.terminal.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.airport.georegion.repository.AirportRepository;
import com.codeshare.airline.master.airport.terminal.eitities.PassengerTerminal;
import com.codeshare.airline.master.airport.terminal.repository.PassengerTerminalRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerTerminalDataLoader {

    private final PassengerTerminalRepository repository;
    private final AirportRepository airportRepository;

    @PostConstruct
    public void load() {

        // Example: load default terminal for each airport
        airportRepository.findAll().forEach(airport -> {

            boolean exists = repository
                    .existsByAirportCodeAndTerminalCode(
                            airport.getIataCode(),
                            "T1");

            if (!exists) {

                PassengerTerminal terminal =
                        new PassengerTerminal();

                terminal.setAirport(airport);
                terminal.setTerminalCode("T1");
                terminal.setTerminalName("Main Terminal");
                terminal.setTerminalType("BOTH");
                terminal.setInternationalFlag(Boolean.TRUE);
                terminal.setStatus(RecordStatus.ACTIVE);

                repository.save(terminal);
            }
        });
    }
}