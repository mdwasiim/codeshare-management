package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.core.enums.common.CabinClass;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftCabinLayout;
import com.codeshare.airline.master.aircraft.entities.AircraftConfiguration;
import com.codeshare.airline.master.aircraft.repository.AircraftCabinLayoutRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(290)
@RequiredArgsConstructor
public class AircraftCabinLayoutLoader implements CommandLineRunner {

    private final AircraftCabinLayoutRepository repository;
    private final AircraftConfigurationRepository configRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AircraftConfiguration qr77w = configRepository.findByConfigurationCode("QR77W-QSUITE").orElseThrow();
        AircraftConfiguration qr359 = configRepository.findByConfigurationCode("QR359-283").orElseThrow();
        AircraftConfiguration qr320 = configRepository.findByConfigurationCode("QR320-ALL-ECO").orElseThrow();
        AircraftConfiguration ba789 = configRepository.findByConfigurationCode("BA789-3C").orElseThrow();

        List<AircraftCabinLayout> layouts = List.of(
                build(qr77w, CabinClass.FIRST, "F", 8, 2, 4, 2, 83, 23, 1),
                build(qr77w, CabinClass.BUSINESS, "C", 42, 11, 4, 6, 79, 21, 2),
                build(qr77w, CabinClass.ECONOMY, "Y", 304, 34, 10, 10, 32, 17, 3),
                build(qr359, CabinClass.BUSINESS, "C", 36, 9, 4, 6, 79, 21, 4),
                build(qr359, CabinClass.ECONOMY, "Y", 247, 28, 9, 8, 31, 18, 5),
                build(qr320, CabinClass.ECONOMY, "Y", 180, 30, 6, 4, 30, 18, 6),
                build(ba789, CabinClass.BUSINESS, "C", 42, 11, 4, 6, 72, 20, 7),
                build(ba789, CabinClass.PREMIUM_ECONOMY, "W", 39, 5, 8, 4, 38, 18, 8),
                build(ba789, CabinClass.ECONOMY, "Y", 135, 15, 9, 6, 31, 17, 9)
        );

        repository.saveAll(layouts);
    }

    private AircraftCabinLayout build(AircraftConfiguration config,
                                      CabinClass cabinClass,
                                      String cabinCode,
                                      int seats,
                                      int rows,
                                      int seatsPerRow,
                                      int crewSeats,
                                      int seatPitch,
                                      int seatWidth,
                                      int displayOrder) {

        AircraftCabinLayout layout = new AircraftCabinLayout();
        layout.setAircraftConfiguration(config);
        layout.setCabinClass(cabinClass);
        layout.setCabinCode(cabinCode);
        layout.setSeatCount(seats);
        layout.setSeatRows(rows);
        layout.setSeatsPerRow(seatsPerRow);
        layout.setCrewSeats(crewSeats);
        layout.setSeatPitchInch(seatPitch);
        layout.setSeatWidthInch(seatWidth);
        layout.setDisplayOrder(displayOrder);
        layout.setActive(Boolean.TRUE);
        layout.setRecordStatus(RecordStatus.ACTIVE);

        return layout;
    }
}
