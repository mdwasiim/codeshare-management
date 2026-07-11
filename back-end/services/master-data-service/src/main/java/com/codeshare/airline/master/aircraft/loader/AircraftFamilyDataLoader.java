package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
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
import java.util.Map;

@Component
@Order(210)
@RequiredArgsConstructor
public class AircraftFamilyDataLoader implements CommandLineRunner {

    private final AircraftFamilyRepository repository;
    private final AircraftManufacturerRepository manufacturerRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) {
            backfillDescriptions();
            return;
        }

        AircraftManufacturer airbus = manufacturerRepository.findByManufacturerCode("AIRBUS").orElseThrow();
        AircraftManufacturer boeing = manufacturerRepository.findByManufacturerCode("BOEING").orElseThrow();

        List<AircraftFamily> families = List.of(
                build("A320", "Airbus A320 Family", airbus, 1, descriptions().get("A320")),
                build("A350", "Airbus A350 XWB Family", airbus, 2, descriptions().get("A350")),
                build("A380", "Airbus A380 Family", airbus, 3, descriptions().get("A380")),
                build("B777", "Boeing 777 Family", boeing, 4, descriptions().get("B777")),
                build("B787", "Boeing 787 Dreamliner Family", boeing, 5, descriptions().get("B787"))
        );

        repository.saveAll(families);
    }

    private AircraftFamily build(String code,
                                 String name,
                                 AircraftManufacturer manufacturer,
                                 int displayOrder,
                                 String description) {

        AircraftFamily family = new AircraftFamily();
        family.setFamilyCode(code);
        family.setFamilyName(name);
        family.setManufacturer(manufacturer);
        family.setDescription(description);
        family.setActive(Boolean.TRUE);
        family.setDisplayOrder(displayOrder);
        family.setRecordStatus(RecordStatus.ACTIVE);
        family.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return family;
    }

    private void backfillDescriptions() {
        descriptions().forEach((code, description) ->
                repository.findByFamilyCode(code).ifPresent(family -> {
                    if (family.getDescription() == null || family.getDescription().isBlank()) {
                        family.setDescription(description);
                        repository.save(family);
                    }
                }));
    }

    private Map<String, String> descriptions() {
        return Map.of(
                "A320", "Narrow-body Airbus family used for short- and medium-haul passenger services.",
                "A350", "Long-range Airbus wide-body family used on intercontinental passenger routes.",
                "A380", "High-capacity Airbus double-deck wide-body family for dense long-haul markets.",
                "B777", "Long-range Boeing wide-body family used for high-capacity international services.",
                "B787", "Boeing Dreamliner wide-body family optimized for long-range fuel-efficient operations."
        );
    }
}
