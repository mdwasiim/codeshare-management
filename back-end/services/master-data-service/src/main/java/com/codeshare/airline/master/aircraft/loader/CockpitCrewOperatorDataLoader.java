package com.codeshare.airline.master.aircraft.loader;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.CockpitCrewOperator;
import com.codeshare.airline.master.aircraft.entities.enums.CrewEmployerType;
import com.codeshare.airline.master.aircraft.repository.CockpitCrewOperatorRepository;
import com.codeshare.airline.master.airline.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.georegion.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(245)
@RequiredArgsConstructor
public class CockpitCrewOperatorDataLoader implements CommandLineRunner {

    private final CockpitCrewOperatorRepository repository;
    private final AirlineCarrierRepository airlineCarrierRepository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<CockpitCrewOperator> operators = List.of(
                build("QR-FLIGHTDECK", "Qatar Airways Flight Deck Crew", CrewEmployerType.AIRLINE, "QR", "QTR", "QA", "QR", 1),
                build("BA-FLIGHTDECK", "British Airways Flight Deck Crew", CrewEmployerType.AIRLINE, "BA", "BAW", "GB", "BA", 2),
                build("WETLEASE-FLIGHTDECK", "Wet Lease Flight Deck Crew Provider", CrewEmployerType.WET_LEASE_OPERATOR, null, null, null, null, 3)
        );

        repository.saveAll(operators);
    }

    private CockpitCrewOperator build(String code,
                                      String name,
                                      CrewEmployerType type,
                                      String iataCode,
                                      String icaoCode,
                                      String iso2CountryCode,
                                      String airlineIataCode,
                                      int displayOrder) {

        CockpitCrewOperator operator = new CockpitCrewOperator();
        operator.setEmployerCode(code);
        operator.setEmployerName(name);
        operator.setEmployerType(type);
        operator.setIataCode(iataCode);
        operator.setIcaoCode(icaoCode);
        operator.setCountry(iso2CountryCode == null ? null : countryRepository.findByIso2Code(iso2CountryCode).orElse(null));
        operator.setAirline(airlineIataCode == null ? null : airlineCarrierRepository.findByIataCode(airlineIataCode).orElse(null));
        operator.setActive(Boolean.TRUE);
        operator.setDisplayOrder(displayOrder);
        operator.setRecordStatus(RecordStatus.ACTIVE);
        operator.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return operator;
    }
}
