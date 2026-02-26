package com.codeshare.airline.data.commercial.codeshare.utils.data;

import com.codeshare.airline.core.enums.codeshare.DeiRuleType;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.commercial.codeshare.eitities.CodeshareDeiRule;
import com.codeshare.airline.data.commercial.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.data.commercial.codeshare.repository.CodeshareDeiRuleRepository;
import com.codeshare.airline.data.commercial.codeshare.repository.CodeshareFlightMappingRepository;
import com.codeshare.airline.data.messaging.eitities.DeiRegistry;
import com.codeshare.airline.data.messaging.repository.DeiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CodeshareDeiRuleLoader implements CommandLineRunner {

    private final CodeshareDeiRuleRepository repository;
    private final CodeshareFlightMappingRepository mappingRepo;
    private final DeiRepository deiRepo;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        CodeshareFlightMapping mapping = mappingRepo.findAll().get(0);
        DeiRegistry disclosureDeiRegistry = deiRepo.findByDeiNumber("002").orElseThrow();

        CodeshareDeiRule rule = new CodeshareDeiRule();
        rule.setFlightMapping(mapping);
        rule.setDeiRegistry(disclosureDeiRegistry);
        rule.setDeiRuleType(DeiRuleType.VALIDATE_ONLY);
        rule.setPriority(1);
        rule.setRecordStatus(RecordStatus.ACTIVE);
        rule.setEffectiveFrom(LocalDate.of(2025, 1, 1));

        repository.save(rule);
    }
}