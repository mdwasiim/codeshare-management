package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftFamily;
import com.codeshare.airline.master.aircraft.entities.AircraftManufacturer;
import com.codeshare.airline.master.aircraft.repository.AircraftFamilyRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(210)
@RequiredArgsConstructor
public class AircraftFamilyDataLoader implements CommandLineRunner {

    private final AircraftFamilyRepository repository;
    private final AircraftManufacturerRepository manufacturerRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AircraftManufacturer airbus = manufacturerRepository.findByManufacturerCode("AIRBUS").orElseThrow();
        AircraftManufacturer boeing = manufacturerRepository.findByManufacturerCode("BOEING").orElseThrow();

        List<AircraftFamily> families = List.of(
                build("A320", "Airbus A320 Family", airbus, 1),
                build("A350", "Airbus A350 XWB Family", airbus, 2),
                build("A380", "Airbus A380 Family", airbus, 3),
                build("B777", "Boeing 777 Family", boeing, 4),
                build("B787", "Boeing 787 Dreamliner Family", boeing, 5)
        );

        repository.saveAll(families);
    }

    private AircraftFamily build(String code,
                                 String name,
                                 AircraftManufacturer manufacturer,
                                 int displayOrder) {

        AircraftFamily family = new AircraftFamily();
        family.setFamilyCode(code);
        family.setFamilyName(name);
        family.setManufacturer(manufacturer);
        family.setActive(Boolean.TRUE);
        family.setDisplayOrder(displayOrder);
        family.setRecordStatus(RecordStatus.ACTIVE);
        family.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return family;
    }
}
