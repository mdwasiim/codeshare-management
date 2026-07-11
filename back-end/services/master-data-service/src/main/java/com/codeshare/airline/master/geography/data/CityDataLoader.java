package com.codeshare.airline.master.geography.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.geography.entities.City;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.entities.State;
import com.codeshare.airline.master.geography.repository.CityRepository;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import com.codeshare.airline.master.geography.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(130)
@RequiredArgsConstructor
public class CityDataLoader implements CommandLineRunner {

    private final CityRepository repository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;

    @Override
    public void run(String... args) {
        Country qa = countryRepository.findByIso2Code("QA").orElseThrow();
        Country gb = countryRepository.findByIso2Code("GB").orElseThrow();
        Country us = countryRepository.findByIso2Code("US").orElseThrow();
        Country ae = countryRepository.findByIso2Code("AE").orElseThrow();
        Country au = countryRepository.findByIso2Code("AU").orElseThrow();

        State ny = stateRepository.findByStateCodeAndCountry_Iso2Code("NY", "US").orElse(null);
        State du = stateRepository.findByStateCodeAndCountry_Iso2Code("DU", "AE").orElse(null);
        State vi = stateRepository.findByStateCodeAndCountry_Iso2Code("VIC", "AU").orElse(null);

        ensure("Doha", "DOH", qa, null);
        ensure("London", "LON", gb, null);
        ensure("New York", "NYC", us, ny);
        ensure("Dubai", "DXB", ae, du);
        ensure("Melbourne", "MEL", au, vi);
    }

    private void ensure(String name, String iataCode, Country country, State state) {
        City city = repository.findByCityName(name).orElseGet(City::new);
        city.setCityName(name);
        city.setIataCityCode(iataCode);
        city.setCountry(country);
        city.setState(state);
        city.setRecordStatus(RecordStatus.ACTIVE);
        city.setEffectiveFrom(LocalDate.of(2000, 1, 1));
        repository.save(city);
    }
}
