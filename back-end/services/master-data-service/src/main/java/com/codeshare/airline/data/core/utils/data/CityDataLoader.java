package com.codeshare.airline.data.core.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.City;
import com.codeshare.airline.data.core.eitities.Country;
import com.codeshare.airline.data.core.eitities.State;
import com.codeshare.airline.data.core.repository.CityRepository;
import com.codeshare.airline.data.core.repository.CountryRepository;
import com.codeshare.airline.data.core.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityDataLoader implements CommandLineRunner {

    private final CityRepository repository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        Country qa = countryRepository.findByIso2Code("QA").orElseThrow();
        Country gb = countryRepository.findByIso2Code("GB").orElseThrow();
        Country us = countryRepository.findByIso2Code("US").orElseThrow();
        Country ae = countryRepository.findByIso2Code("AE").orElseThrow();

        State ny = stateRepository.findByStateCodeAndCountry_Iso2Code("NY", "US").orElse(null);
        State du = stateRepository.findByStateCodeAndCountry_Iso2Code("DU", "AE").orElse(null);

        List<City> cities = List.of(

                build("Doha", "DOH", qa, null),
                build("London", "LON", gb, null),
                build("New York", "NYC", us, ny),
                build("Dubai", "DXB", ae, du)
        );

        repository.saveAll(cities);
    }

    private City build(String name,
                       String iataCode,
                       Country country,
                       State state) {

        City city = new City();
        city.setCityName(name);
        city.setIataCityCode(iataCode);
        city.setCountry(country);
        city.setState(state);
        city.setStatusCode(Status.ACTIVE);
        city.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return city;
    }
}