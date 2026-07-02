package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftFamily;
import com.codeshare.airline.master.aircraft.entities.AircraftType;
import com.codeshare.airline.master.aircraft.entities.enums.AircraftCategory;
import com.codeshare.airline.master.aircraft.repository.AircraftFamilyRepository;
import com.codeshare.airline.master.aircraft.repository.AircraftTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(220)
@RequiredArgsConstructor
public class AircraftTypeDataLoader implements CommandLineRunner {

    private final AircraftTypeRepository repository;
    private final AircraftFamilyRepository familyRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AircraftFamily a320 = familyRepository.findByFamilyCode("A320").orElseThrow();
        AircraftFamily a350 = familyRepository.findByFamilyCode("A350").orElseThrow();
        AircraftFamily a380 = familyRepository.findByFamilyCode("A380").orElseThrow();
        AircraftFamily b777 = familyRepository.findByFamilyCode("B777").orElseThrow();
        AircraftFamily b787 = familyRepository.findByFamilyCode("B787").orElseThrow();

        List<AircraftType> types = List.of(
                build(b777, "Boeing 777-300ER", "B777-300ER", "B77W", "77W",
                        "GE90-115B", 396, 13650, 351534, true, false, true, false, 1,
                        "IATA aircraft type 77W, ICAO designator B77W."),
                build(b787, "Boeing 787-8 Dreamliner", "B787-8", "B788", "788",
                        "GEnx / Trent 1000", 242, 13620, 227930, true, false, true, false, 2,
                        "IATA aircraft type 788, ICAO designator B788."),
                build(b787, "Boeing 787-9 Dreamliner", "B787-9", "B789", "789",
                        "GEnx / Trent 1000", 290, 14140, 254011, true, false, true, true, 3,
                        "IATA aircraft type 789, ICAO designator B789."),
                build(a320, "Airbus A320-200", "A320-200", "A320", "320",
                        "CFM56 / V2500", 180, 6150, 78000, false, false, false, true, 4,
                        "IATA aircraft type 320, ICAO designator A320."),
                build(a320, "Airbus A321neo", "A321NEO", "A21N", "32Q",
                        "LEAP-1A / PW1100G", 206, 7400, 97000, false, false, true, true, 5,
                        "IATA aircraft type 32Q, ICAO designator A21N."),
                build(a350, "Airbus A350-900", "A350-900", "A359", "359",
                        "Trent XWB-84", 325, 15000, 280000, true, false, true, true, 6,
                        "IATA aircraft type 359, ICAO designator A359."),
                build(a380, "Airbus A380-800", "A380-800", "A388", "388",
                        "Trent 900 / GP7200", 517, 14800, 575000, true, false, true, false, 7,
                        "IATA aircraft type 388, ICAO designator A388.")
        );

        repository.saveAll(types);
    }

    private AircraftType build(AircraftFamily family,
                               String aircraftName,
                               String model,
                               String icaoCode,
                               String iataCode,
                               String engineType,
                               int typicalSeatCapacity,
                               int maxRangeKm,
                               int maxTakeoffWeightKg,
                               boolean wideBody,
                               boolean freighter,
                               boolean etopsCertified,
                               boolean inProduction,
                               int displayOrder,
                               String description) {
        AircraftType type = new AircraftType();
        type.setAircraftFamily(family);
        type.setAircraftName(aircraftName);
        type.setModel(model);
        type.setIcaoCode(icaoCode);
        type.setIataCode(iataCode);
        type.setEngineType(engineType);
        type.setCategory(AircraftCategory.PASSENGER);
        type.setTypicalSeatCapacity(typicalSeatCapacity);
        type.setMaxRangeKm(maxRangeKm);
        type.setMaxTakeoffWeightKg(maxTakeoffWeightKg);
        type.setWideBody(wideBody);
        type.setFreighter(freighter);
        type.setEtopsCertified(etopsCertified);
        type.setInProduction(inProduction);
        type.setDisplayOrder(displayOrder);
        type.setActive(Boolean.TRUE);
        type.setDescription(description);
        type.setRecordStatus(RecordStatus.ACTIVE);
        type.setEffectiveFrom(LocalDate.of(2000, 1, 1));
        return type;
    }
}
