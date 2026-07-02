package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.AircraftOwner;
import com.codeshare.airline.core.enums.master.aircraft.AircraftOwnerType;
import com.codeshare.airline.master.aircraft.repository.AircraftOwnerRepository;
import com.codeshare.airline.master.georegion.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(230)
@RequiredArgsConstructor
public class AircraftOwnerDataLoader implements CommandLineRunner {

    private final AircraftOwnerRepository repository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<AircraftOwner> owners = List.of(
                build("QR", "Qatar Airways", AircraftOwnerType.AIRLINE, "QR", "QTR", "QA", 1),
                build("BA", "British Airways", AircraftOwnerType.AIRLINE, "BA", "BAW", "GB", 2),
                build("AERCAP", "AerCap Ireland Limited", AircraftOwnerType.LEASING_COMPANY, null, null, "IE", 3),
                build("DAE", "Dubai Aerospace Enterprise", AircraftOwnerType.LEASING_COMPANY, null, null, "AE", 4)
        );

        repository.saveAll(owners);
    }

    private AircraftOwner build(String code,
                                String name,
                                AircraftOwnerType type,
                                String iataCode,
                                String icaoCode,
                                String iso2CountryCode,
                                int displayOrder) {

        AircraftOwner owner = new AircraftOwner();
        owner.setOwnerCode(code);
        owner.setOwnerName(name);
        owner.setOwnerType(type);
        owner.setIataCode(iataCode);
        owner.setIcaoCode(icaoCode);
        owner.setCountry(countryRepository.findByIso2Code(iso2CountryCode).orElse(null));
        owner.setActive(Boolean.TRUE);
        owner.setDisplayOrder(displayOrder);
        owner.setRecordStatus(RecordStatus.ACTIVE);
        owner.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return owner;
    }
}
