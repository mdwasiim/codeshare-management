package com.codeshare.airline.data.aircraft.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.aircraft.eitities.AircraftConfiguration;
import com.codeshare.airline.data.aircraft.eitities.AircraftType;
import com.codeshare.airline.data.aircraft.repository.AircraftConfigurationRepository;
import com.codeshare.airline.data.aircraft.repository.AircraftTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AircraftConfigurationLoader implements CommandLineRunner {

    private final AircraftConfigurationRepository repository;
    private final AircraftTypeRepository typeRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AircraftType b77w = typeRepository.findByIataCode("77W").orElseThrow();
        AircraftType a320 = typeRepository.findByIataCode("320").orElseThrow();

        List<AircraftConfiguration> configs = List.of(
                build("77W-QSUITE", b77w, "B777-300ER Qsuite Layout"),
                build("320-ALL-ECO", a320, "A320 All Economy Layout")
        );

        repository.saveAll(configs);
    }

    private AircraftConfiguration build(String code,
                                        AircraftType type,
                                        String description) {

        AircraftConfiguration config = new AircraftConfiguration();
        config.setConfigurationCode(code);
        config.setAircraftType(type);
        config.setDescription(description);
        config.setStatus(Status.ACTIVE);

        return config;
    }
}