package com.codeshare.airline.data.core.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.Country;
import com.codeshare.airline.data.core.eitities.Region;
import com.codeshare.airline.data.core.repository.CountryRepository;
import com.codeshare.airline.data.core.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CountryDataLoader implements CommandLineRunner {

    private final CountryRepository repository;
    private final RegionRepository regionRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        Region mea = regionRepository.findByRegionCode("MEA").orElseThrow();
        Region eur = regionRepository.findByRegionCode("EUR").orElseThrow();
        Region nam = regionRepository.findByRegionCode("NAM").orElseThrow();
        Region apac = regionRepository.findByRegionCode("APAC").orElseThrow();

        List<Country> countries = List.of(

                build("QA", "QAT", "Qatar", mea),
                build("GB", "GBR", "United Kingdom", eur),
                build("AE", "ARE", "United Arab Emirates", mea),
                build("US", "USA", "United States", nam),
                build("IN", "IND", "India", apac)
        );

        repository.saveAll(countries);
    }

    private Country build(String iso2,
                          String iso3,
                          String name,
                          Region region) {

        Country country = new Country();
        country.setIso2Code(iso2);
        country.setIso3Code(iso3);
        country.setCountryName(name);
        country.setRegion(region);
        country.setStatusCode(Status.ACTIVE);
        country.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return country;
    }
}