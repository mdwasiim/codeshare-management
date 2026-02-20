package com.codeshare.airline.data.aircraft.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.aircraft.eitities.AircraftConfiguration;
import com.codeshare.airline.data.aircraft.eitities.AirlineFleet;
import com.codeshare.airline.data.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.data.aircraft.repository.AirlineFleetRepository;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.data.core.repository.AirlineCarrierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AirlineFleetDataLoader implements CommandLineRunner {

    private final AirlineFleetRepository repository;
    private final AirlineCarrierRepository airlineRepository;
    private final AircraftConfigurationRepository configurationRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AirlineCarrier qr = airlineRepository.findByIataCode("QR").orElseThrow();
        AirlineCarrier ba = airlineRepository.findByIataCode("BA").orElseThrow();

        AircraftConfiguration qr77w =
                configurationRepository.findByConfigurationCode("77W-QSUITE").orElseThrow();

        AircraftConfiguration qr320 =
                configurationRepository.findByConfigurationCode("320-ALL-ECO").orElseThrow();

        List<AirlineFleet> fleet = List.of(

                build(qr, qr77w, 25),
                build(qr, qr320, 40),
                build(ba, qr77w, 12)
        );

        repository.saveAll(fleet);
    }

    private AirlineFleet build(AirlineCarrier airline,
                               AircraftConfiguration config,
                               int count) {

        AirlineFleet fleet = new AirlineFleet();
        fleet.setAirline(airline);
        fleet.setAircraftConfiguration(config);
        fleet.setAircraftCount(count);
        fleet.setStatusCode(Status.ACTIVE);
        fleet.setEffectiveFrom(LocalDate.of(2020, 1, 1));

        return fleet;
    }
}