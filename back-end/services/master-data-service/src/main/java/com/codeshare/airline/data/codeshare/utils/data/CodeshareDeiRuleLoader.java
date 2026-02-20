package com.codeshare.airline.data.codeshare.utils.data;

import com.codeshare.airline.core.enums.codeshare.DeiRuleType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.codeshare.eitities.CodeshareDeiRule;
import com.codeshare.airline.data.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.data.ssim.eitities.Dei;
import com.codeshare.airline.data.codeshare.repository.CodeshareDeiRuleRepository;
import com.codeshare.airline.data.codeshare.repository.CodeshareFlightMappingRepository;
import com.codeshare.airline.data.ssim.repository.DeiRepository;
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
        Dei disclosureDei = deiRepo.findByDeiNumber("002").orElseThrow();

        CodeshareDeiRule rule = new CodeshareDeiRule();
        rule.setFlightMapping(mapping);
        rule.setDei(disclosureDei);
        rule.setDeiRuleType(DeiRuleType.VALIDATE_ONLY);
        rule.setPriority(1);
        rule.setStatusCode(Status.ACTIVE);
        rule.setEffectiveFrom(LocalDate.of(2025, 1, 1));

        repository.save(rule);
    }
}