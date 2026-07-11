package com.codeshare.airline.master.geography.data;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.master.geography.entities.Airport;
import com.codeshare.airline.master.geography.entities.City;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.AirportRepository;
import com.codeshare.airline.master.geography.repository.CityRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import com.codeshare.airline.master.geography.repository.TimezoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Order(140)
@RequiredArgsConstructor
public class AirportDataLoader implements CommandLineRunner {

    private final AirportRepository repository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final TimezoneRepository timezoneRepository;

    @Override
    public void run(String... args) {
        City doha = cityRepository.findByCityName("Doha").orElseThrow();
        City london = cityRepository.findByCityName("London").orElseThrow();
        City newYork = cityRepository.findByCityName("New York").orElseThrow();
        City dubai = cityRepository.findByCityName("Dubai").orElseThrow();
        City melbourne = cityRepository.findByCityName("Melbourne").orElseThrow();

        Country qa = countryRepository.findByIso2Code("QA").orElseThrow();
        Country gb = countryRepository.findByIso2Code("GB").orElseThrow();
        Country us = countryRepository.findByIso2Code("US").orElseThrow();
        Country ae = countryRepository.findByIso2Code("AE").orElseThrow();
        Country au = countryRepository.findByIso2Code("AU").orElseThrow();

        Timezone tzQatar = timezoneRepository.findByTzIdentifier("Asia/Qatar").orElseThrow();
        Timezone tzLondon = timezoneRepository.findByTzIdentifier("Europe/London").orElseThrow();
        Timezone tzNY = timezoneRepository.findByTzIdentifier("America/New_York").orElseThrow();
        Timezone tzDubai = timezoneRepository.findByTzIdentifier("Asia/Dubai").orElseThrow();
        Timezone tzMelbourne = timezoneRepository.findByTzIdentifier("Australia/Melbourne").orElseThrow();

        ensure("DOH", "OTHH",
                "Hamad International Airport",
                doha, qa, tzQatar,
                25.2730567, 51.6080567,
                13, true, true);

        ensure("LHR", "EGLL",
                "London Heathrow Airport",
                london, gb, tzLondon,
                51.4700200, -0.4542950,
                83, true, true);

        ensure("JFK", "KJFK",
                "John F. Kennedy International Airport",
                newYork, us, tzNY,
                40.6413111, -73.7781391,
                13, true, true);

        ensure("DXB", "OMDB",
                "Dubai International Airport",
                dubai, ae, tzDubai,
                25.2531745, 55.3656728,
                62, true, true);

        ensure("MEL", "YMML",
                "Melbourne Airport",
                melbourne, au, tzMelbourne,
                -37.6690123, 144.8410273,
                434, true, true);
    }

    private void ensure(String iata,
                        String icao,
                        String name,
                        City city,
                        Country country,
                        Timezone timezone,
                        double lat,
                        double lon,
                        int elevation,
                        boolean international,
                        boolean hub) {
        Airport airport = repository.findByIataCode(iata).orElseGet(Airport::new);

        airport.setIataCode(iata);
        airport.setIcaoCode(icao);
        airport.setAirportName(name);

        airport.setCity(city);
        airport.setCountry(country);
        airport.setTimezone(timezone);

        airport.setLatitude(BigDecimal.valueOf(lat));
        airport.setLongitude(BigDecimal.valueOf(lon));
        airport.setElevationFeet(elevation);

        airport.setInternational(international);
        airport.setHub(hub);

        airport.setRecordStatus(RecordStatus.ACTIVE);
        airport.setEffectiveFrom(LocalDate.of(2000, 1, 1));
        repository.save(airport);
    }
}
