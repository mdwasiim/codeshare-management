package com.codeshare.airline.master.commercial.codeshare.data;

import com.codeshare.airline.core.enums.common.FlightNumberPattern;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareDayRule;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.master.commercial.codeshare.repository.CodeshareDayRuleRepository;
import com.codeshare.airline.master.commercial.codeshare.repository.CodeshareFlightMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CodeshareDayRuleLoader implements CommandLineRunner {

    private final CodeshareDayRuleRepository repository;
    private final CodeshareFlightMappingRepository mappingRepo;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        CodeshareFlightMapping mapping = mappingRepo.findAll().get(0);

        CodeshareDayRule rule = new CodeshareDayRule();
        rule.setFlightMapping(mapping);
        rule.setFlightNumberPattern(FlightNumberPattern.BOTH);
        rule.setOperatingDays("1111111");
        rule.setRecordStatus(RecordStatus.ACTIVE);
        rule.setEffectiveFrom(LocalDate.of(2025, 1, 1));

        repository.save(rule);
    }
}