package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.entities.AirlineFleetProfile;
import com.codeshare.airline.platform.core.enums.master.aircraft.FleetStatus;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AirlineFleetRepository;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(300)
@RequiredArgsConstructor
public class AirlineFleetDataLoader implements CommandLineRunner {

    private final AirlineFleetRepository repository;
    private final AirlineCarrierRepository airlineCarrierRepository;
    private final AircraftConfigurationRepository configurationRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AirlineCarrier qr = airlineCarrierRepository.findByIataCode("QR").orElseThrow();
        AirlineCarrier ba = airlineCarrierRepository.findByIataCode("BA").orElseThrow();

        AircraftConfiguration qr77w = configurationRepository.findByConfigurationCode("QR77W-QSUITE").orElseThrow();
        AircraftConfiguration qr359 = configurationRepository.findByConfigurationCode("QR359-283").orElseThrow();
        AircraftConfiguration qr320 = configurationRepository.findByConfigurationCode("QR320-ALL-ECO").orElseThrow();
        AircraftConfiguration ba789 = configurationRepository.findByConfigurationCode("BA789-3C").orElseThrow();

        List<AirlineFleetProfile> fleet = List.of(
                build(qr, qr77w, 44, 40, true, 1, "Qatar Airways Boeing 777-300ER fleet profile."),
                build(qr, qr359, 34, 32, true, 2, "Qatar Airways Airbus A350-900 fleet profile."),
                build(qr, qr320, 29, 26, false, 3, "Qatar Airways Airbus A320 short-haul fleet profile."),
                build(ba, ba789, 18, 18, true, 4, "British Airways Boeing 787-9 fleet profile.")
        );

        repository.saveAll(fleet);
    }

    private AirlineFleetProfile build(AirlineCarrier airline,
                                      AircraftConfiguration config,
                                      int plannedCount,
                                      int activeCount,
                                      boolean defaultConfiguration,
                                      int displayOrder,
                                      String description) {

        AirlineFleetProfile fleet = new AirlineFleetProfile();
        fleet.setAirline(airline);
        fleet.setAircraftType(config.getAircraftType());
        fleet.setAircraftConfiguration(config);
        fleet.setPlannedAircraftCount(plannedCount);
        fleet.setActiveAircraftCount(activeCount);
        fleet.setFleetStatus(FleetStatus.ACTIVE);
        fleet.setDefaultConfiguration(defaultConfiguration);
        fleet.setActive(Boolean.TRUE);
        fleet.setDisplayOrder(displayOrder);
        fleet.setDescription(description);
        fleet.setRecordStatus(RecordStatus.ACTIVE);
        fleet.setEffectiveFrom(LocalDate.of(2020, 1, 1));

        return fleet;
    }
}
