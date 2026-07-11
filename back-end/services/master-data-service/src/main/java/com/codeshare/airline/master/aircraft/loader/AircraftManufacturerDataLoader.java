package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftManufacturer;
import com.codeshare.airline.master.aircraft.repository.AircraftManufacturerRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(200)
@RequiredArgsConstructor
public class AircraftManufacturerDataLoader implements CommandLineRunner {

    private final AircraftManufacturerRepository repository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<AircraftManufacturer> manufacturers = List.of(
                build("AIRBUS", "Airbus", "Airbus", "FR", "https://www.airbus.com", 1),
                build("BOEING", "The Boeing Company", "Boeing", "US", "https://www.boeing.com", 2),
                build("EMBRAER", "Embraer S.A.", "Embraer", "BR", "https://embraer.com", 3),
                build("ATR", "Avions de Transport Regional", "ATR", "FR", "https://www.atr-aircraft.com", 4)
        );

        repository.saveAll(manufacturers);
    }

    private AircraftManufacturer build(String code,
                                       String name,
                                       String shortName,
                                       String iso2CountryCode,
                                       String website,
                                       int displayOrder) {

        AircraftManufacturer manufacturer = new AircraftManufacturer();
        manufacturer.setManufacturerCode(code);
        manufacturer.setManufacturerName(name);
        manufacturer.setShortName(shortName);
        manufacturer.setCountry(countryRepository.findByIso2Code(iso2CountryCode).orElse(null));
        manufacturer.setWebsite(website);
        manufacturer.setActive(Boolean.TRUE);
        manufacturer.setDisplayOrder(displayOrder);
        manufacturer.setRecordStatus(RecordStatus.ACTIVE);
        manufacturer.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return manufacturer;
    }
}
