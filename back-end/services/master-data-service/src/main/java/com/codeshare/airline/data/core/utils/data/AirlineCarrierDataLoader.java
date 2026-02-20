package com.codeshare.airline.data.core.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.data.core.repository.AirlineCarrierRepository;
import com.codeshare.airline.data.core.eitities.Country;
import com.codeshare.airline.data.core.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AirlineCarrierDataLoader implements CommandLineRunner {

    private final AirlineCarrierRepository repository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        Country qa = countryRepository.findByIsoCode("QA").orElseThrow();
        Country gb = countryRepository.findByIsoCode("GB").orElseThrow();
        Country ae = countryRepository.findByIsoCode("AE").orElseThrow();
        Country us = countryRepository.findByIsoCode("US").orElseThrow();

        List<AirlineCarrier> carriers = List.of(

                buildCarrier(
                        "Qatar Airways Q.C.S.C.",
                        "Qatar Airways",
                        "QR",
                        "QTR",
                        "QATARI",
                        qa
                ),

                buildCarrier(
                        "British Airways Plc",
                        "British Airways",
                        "BA",
                        "BAW",
                        "SPEEDBIRD",
                        gb
                ),

                buildCarrier(
                        "Emirates",
                        "Emirates",
                        "EK",
                        "UAE",
                        "EMIRATES",
                        ae
                ),

                buildCarrier(
                        "American Airlines Inc.",
                        "American Airlines",
                        "AA",
                        "AAL",
                        "AMERICAN",
                        us
                )
        );

        repository.saveAll(carriers);
    }

    private AirlineCarrier buildCarrier(String legalName,
                                        String commercialName,
                                        String iata,
                                        String icao,
                                        String callsign,
                                        Country country) {

        AirlineCarrier carrier = new AirlineCarrier();
        carrier.setLegalName(legalName);
        carrier.setCommercialName(commercialName);
        carrier.setIataCode(iata);
        carrier.setIcaoCode(icao);
        carrier.setCallsign(callsign);
        carrier.setCountry(country);
        carrier.setStatus(Status.ACTIVE);
        carrier.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return carrier;
    }
}