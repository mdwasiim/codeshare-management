package com.codeshare.airline.master.geography.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.geography.entities.Country;
import com.codeshare.airline.master.geography.entities.Region;
import com.codeshare.airline.master.geography.repository.CountryRepository;
import com.codeshare.airline.master.geography.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(110)
@RequiredArgsConstructor
public class CountryDataLoader implements CommandLineRunner {

    private final CountryRepository repository;
    private final RegionRepository regionRepository;

    @Override
    public void run(String... args) {
        Region mea = regionRepository.findByRegionCode("MEA").orElseThrow();
        Region eur = regionRepository.findByRegionCode("EUR").orElseThrow();
        Region nam = regionRepository.findByRegionCode("NAM").orElseThrow();
        Region apac = regionRepository.findByRegionCode("APAC").orElseThrow();

        ensure("QA", "QAT", "Qatar", mea);
        ensure("GB", "GBR", "United Kingdom", eur);
        ensure("AE", "ARE", "United Arab Emirates", mea);
        ensure("US", "USA", "United States", nam);
        ensure("AU", "AUS", "Australia", apac);
        ensure("IN", "IND", "India", apac);
    }

    private void ensure(String iso2, String iso3, String name, Region region) {
        Country country = repository.findByIso2Code(iso2).orElseGet(Country::new);
        country.setIso2Code(iso2);
        country.setIso3Code(iso3);
        country.setCountryName(name);
        country.setRegion(region);
        country.setRecordStatus(RecordStatus.ACTIVE);
        country.setEffectiveFrom(LocalDate.of(2000, 1, 1));
        repository.save(country);
    }
}
