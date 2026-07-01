package com.codeshare.airline.master.georegion.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.georegion.eitities.Country;
import com.codeshare.airline.master.georegion.eitities.State;
import com.codeshare.airline.master.georegion.repository.CountryRepository;
import com.codeshare.airline.master.georegion.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StateDataLoader implements CommandLineRunner {

    private final StateRepository repository;
    private final CountryRepository countryRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        Country us = countryRepository.findByIso2Code("US").orElseThrow();
        Country in = countryRepository.findByIso2Code("IN").orElseThrow();
        Country ae = countryRepository.findByIso2Code("AE").orElseThrow();

        List<State> states = List.of(

                build("NY", "New York", us),
                build("CA", "California", us),
                build("DL", "Delhi", in),
                build("MH", "Maharashtra", in),
                build("DU", "Dubai Emirate", ae)
        );

        repository.saveAll(states);
    }

    private State build(String code,
                        String name,
                        Country country) {

        State state = new State();
        state.setStateCode(code);
        state.setStateName(name);
        state.setCountry(country);
        state.setRecordStatus(RecordStatus.ACTIVE);
        state.setEffectiveFrom(LocalDate.of(2000, 1, 1));

        return state;
    }
}