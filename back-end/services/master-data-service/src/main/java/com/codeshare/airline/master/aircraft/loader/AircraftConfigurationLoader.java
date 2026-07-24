package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.master.aircraft.entities.*;
import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.CockpitCrewEmployer;
import com.codeshare.airline.platform.core.enums.master.aircraft.ConfigurationSource;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftOwnerRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.master.aircraft.repository.CabinCrewOperatorRepository;
import com.codeshare.airline.master.aircraft.repository.CockpitCrewOperatorRepository;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(260)
@RequiredArgsConstructor
public class AircraftConfigurationLoader implements CommandLineRunner {

    private final AircraftConfigurationRepository repository;
    private final AircraftTypeRepository typeRepository;
    private final AirlineRepository airlineRepository;
    private final AircraftOwnerRepository ownerRepository;
    private final CabinCrewOperatorRepository cabinCrewOperatorRepository;
    private final CockpitCrewOperatorRepository cockpitCrewOperatorRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AircraftType b77w = typeRepository.findByIataCode("77W").orElseThrow();
        AircraftType b789 = typeRepository.findByIataCode("789").orElseThrow();
        AircraftType a320 = typeRepository.findByIataCode("320").orElseThrow();
        AircraftType a359 = typeRepository.findByIataCode("359").orElseThrow();

        Airline qr = airlineRepository.findByIataCode("QR").orElseThrow();
        Airline ba = airlineRepository.findByIataCode("BA").orElseThrow();

        AircraftOwner qrOwner = ownerRepository.findByOwnerCode("QR").orElse(null);
        AircraftOwner baOwner = ownerRepository.findByOwnerCode("BA").orElse(null);

        CabinCrewEmployer qrCabin = cabinCrewOperatorRepository.findByEmployerCode("QR-CABIN").orElse(null);
        CabinCrewEmployer baCabin = cabinCrewOperatorRepository.findByEmployerCode("BA-CABIN").orElse(null);
        CockpitCrewEmployer qrCockpit = cockpitCrewOperatorRepository.findByEmployerCode("QR-FLIGHTDECK").orElse(null);
        CockpitCrewEmployer baCockpit = cockpitCrewOperatorRepository.findByEmployerCode("BA-FLIGHTDECK").orElse(null);

        List<AircraftConfiguration> configs = List.of(
                build("QR77W-QSUITE", "Qatar Airways 777-300ER Qsuite", b77w, qr, qrOwner,
                        qrCabin, qrCockpit, 354, 23200, 1,
                        "Four-class Boeing 777-300ER configuration with Qsuite business cabin."),
                build("QR359-283", "Qatar Airways A350-900 283 Seats", a359, qr, qrOwner,
                        qrCabin, qrCockpit, 283, 21000, 2,
                        "Long-haul Airbus A350-900 configuration."),
                build("QR320-ALL-ECO", "Qatar Airways A320 All Economy", a320, qr, qrOwner,
                        qrCabin, qrCockpit, 180, 3500, 3,
                        "Single-class Airbus A320 short-haul configuration."),
                build("BA789-3C", "British Airways 787-9 Three Cabin", b789, ba, baOwner,
                        baCabin, baCockpit, 216, 18000, 4,
                        "Three-cabin Boeing 787-9 configuration.")
        );

        repository.saveAll(configs);
    }

    private AircraftConfiguration build(String code,
                                        String name,
                                        AircraftType type,
                                        Airline airline,
                                        AircraftOwner owner,
                                        CabinCrewEmployer cabinCrewEmployer,
                                        CockpitCrewEmployer cockpitCrewEmployer,
                                        int totalSeats,
                                        int cargoCapacityKg,
                                        int displayOrder,
                                        String description) {

        AircraftConfiguration config = new AircraftConfiguration();
        config.setConfigurationCode(code);
        config.setConfigurationName(name);
        config.setAircraftType(type);
        config.setAirline(airline);
        config.setAircraftOwner(owner);
        config.setCabinCrewEmployer(cabinCrewEmployer);
        config.setCockpitCrewEmployer(cockpitCrewEmployer);
        config.setConfigurationSource(ConfigurationSource.MANUAL);
        config.setTotalSeats(totalSeats);
        config.setCargoCapacityKg(cargoCapacityKg);
        config.setActive(Boolean.TRUE);
        config.setDisplayOrder(displayOrder);
        config.setDescription(description);
        config.setRecordStatus(RecordStatus.ACTIVE);
        config.setEffectiveFrom(LocalDate.of(2020, 1, 1));

        return config;
    }
}
