package com.codeshare.airline.master.airlines.loader;

import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.airlines.repository.AirlineRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(120)
@RequiredArgsConstructor
public class AirlineCarrierDataLoader implements CommandLineRunner {

    private final AirlineRepository repository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {
        ensure("QR", "QTR", "157", "Qatar Airways Company Q.C.S.C.",
                "Qatar Airways", "Qatar Airways", "QATAR", "QA",
                "https://www.qatarairways.com", 1);
        ensure("BA", "BAW", "125", "British Airways Plc",
                "British Airways", "British Airways", "SPEEDBIRD", "GB",
                "https://www.britishairways.com", 2);
        ensure("AA", "AAL", "001", "American Airlines, Inc.",
                "American Airlines", "American Airlines", "AMERICAN", "US",
                "https://www.aa.com", 3);
        ensure("VA", "VOZ", "795", "Virgin Australia Airlines Pty Ltd",
                "Virgin Australia", "Virgin Australia", "VIRGIN", "AU",
                "https://www.virginaustralia.com", 4);
        ensure("EK", "UAE", "176", "Emirates",
                "Emirates", "Emirates", "EMIRATES", "AE",
                "https://www.emirates.com", 5);
    }

    private void ensure(String iataCode,
                        String icaoCode,
                        String numericCode,
                        String legalName,
                        String commercialName,
                        String displayName,
                        String callsign,
                        String iso2CountryCode,
                        String website,
                        int displayOrder) {
        Airline carrier = repository.findByIataCode(iataCode).orElseGet(Airline::new);
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
        repository.save(carrier);
    }
}
