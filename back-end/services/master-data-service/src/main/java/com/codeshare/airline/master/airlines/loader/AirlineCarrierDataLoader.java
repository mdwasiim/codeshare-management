package com.codeshare.airline.master.airlines.loader;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(120)
@RequiredArgsConstructor
public class AirlineCarrierDataLoader implements CommandLineRunner {

    private final AirlineCarrierRepository repository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<AirlineCarrier> carriers = List.of(
                build("QR", "QTR", "157", "Qatar Airways Company Q.C.S.C.",
                        "Qatar Airways", "Qatar Airways", "QATAR", "QA",
                        "https://www.qatarairways.com", 1),
                build("BA", "BAW", "125", "British Airways Plc",
                        "British Airways", "British Airways", "SPEEDBIRD", "GB",
                        "https://www.britishairways.com", 2),
                build("EK", "UAE", "176", "Emirates",
                        "Emirates", "Emirates", "EMIRATES", "AE",
                        "https://www.emirates.com", 3)
        );

        repository.saveAll(carriers);
    }

    private AirlineCarrier build(String iataCode,
                                 String icaoCode,
                                 String numericCode,
                                 String legalName,
                                 String commercialName,
                                 String displayName,
                                 String callsign,
                                 String iso2CountryCode,
                                 String website,
                                 int displayOrder) {

        AirlineCarrier carrier = new AirlineCarrier();
        carrier.setIataCode(iataCode);
        carrier.setIcaoCode(icaoCode);
        carrier.setIataNumericCode(numericCode);
        carrier.setLegalName(legalName);
        carrier.setCommercialName(commercialName);
        carrier.setDisplayName(displayName);
        carrier.setCallsign(callsign);
        carrier.setCountry(countryRepository.findByIso2Code(iso2CountryCode).orElse(null));
        carrier.setWebsite(website);
        carrier.setActive(Boolean.TRUE);
        carrier.setDisplayOrder(displayOrder);
        carrier.setRecordStatus(RecordStatus.ACTIVE);
        carrier.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return carrier;
    }
}
