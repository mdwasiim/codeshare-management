package com.codeshare.airline.data.aircraft.utils.data;

import com.codeshare.airline.core.enums.common.CabinClass;
import com.codeshare.airline.data.aircraft.eitities.AircraftCabinLayout;
import com.codeshare.airline.data.aircraft.eitities.AircraftConfiguration;
import com.codeshare.airline.data.aircraft.repository.AircraftCabinLayoutRepository;
import com.codeshare.airline.data.aircraft.repository.AircraftConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AircraftCabinLayoutLoader implements CommandLineRunner {

    private final AircraftCabinLayoutRepository repository;
    private final AircraftConfigurationRepository configRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AircraftConfiguration config =
                configRepository.findByConfigurationCode("77W-QSUITE")
                        .orElseThrow();

        List<AircraftCabinLayout> layouts = List.of(

                build(config, CabinClass.BUSINESS, 42),
                build(config, CabinClass.ECONOMY, 312)
        );

        repository.saveAll(layouts);
    }

    private AircraftCabinLayout build(AircraftConfiguration config,
                                      CabinClass cabinClass,
                                      int seats) {

        AircraftCabinLayout layout = new AircraftCabinLayout();
        layout.setAircraftConfiguration(config);
        layout.setCabinClass(cabinClass);
        layout.setSeatCount(seats);

        return layout;
    }
}