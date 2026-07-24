package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.CabinCrewEmployer;
import com.codeshare.airline.platform.core.enums.master.aircraft.CrewEmployerType;
import com.codeshare.airline.master.aircraft.repository.CabinCrewOperatorRepository;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(240)
@RequiredArgsConstructor
public class CabinCrewOperatorDataLoader implements CommandLineRunner {

    private final CabinCrewOperatorRepository repository;
    private final AirlineRepository airlineRepository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<CabinCrewEmployer> operators = List.of(
                build("QR-CABIN", "Qatar Airways Cabin Crew", CrewEmployerType.AIRLINE, "QR", "QTR", "QA", "QR", 1),
                build("BA-CABIN", "British Airways Cabin Crew", CrewEmployerType.AIRLINE, "BA", "BAW", "GB", "BA", 2),
                build("WETLEASE-CABIN", "Wet Lease Cabin Crew Provider", CrewEmployerType.WET_LEASE_OPERATOR, null, null, null, null, 3)
        );

        repository.saveAll(operators);
    }

    private CabinCrewEmployer build(String code,
                                    String name,
                                    CrewEmployerType type,
                                    String iataCode,
                                    String icaoCode,
                                    String iso2CountryCode,
                                    String airlineIataCode,
                                    int displayOrder) {

        CabinCrewEmployer operator = new CabinCrewEmployer();
        operator.setEmployerCode(code);
        operator.setEmployerName(name);
        operator.setEmployerType(type);
        operator.setIataCode(iataCode);
        operator.setIcaoCode(icaoCode);
        operator.setCountry(iso2CountryCode == null ? null : countryRepository.findByIso2Code(iso2CountryCode).orElse(null));
        operator.setAirline(airlineIataCode == null ? null : airlineRepository.findByIataCode(airlineIataCode).orElse(null));
        operator.setActive(Boolean.TRUE);
        operator.setDisplayOrder(displayOrder);
        operator.setRecordStatus(RecordStatus.ACTIVE);
        operator.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return operator;
    }
}
