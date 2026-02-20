package com.codeshare.airline.data.codeshare.utils.data;

import com.codeshare.airline.core.enums.codeshare.EquipmentRuleType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.aircraft.eitities.AircraftType;
import com.codeshare.airline.data.aircraft.repository.AircraftTypeRepository;
import com.codeshare.airline.data.codeshare.eitities.CodeshareEquipmentRule;
import com.codeshare.airline.data.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.data.codeshare.repository.CodeshareEquipmentRuleRepository;
import com.codeshare.airline.data.codeshare.repository.CodeshareFlightMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CodeshareEquipmentRuleLoader implements CommandLineRunner {

    private final CodeshareEquipmentRuleRepository repository;
    private final CodeshareFlightMappingRepository mappingRepo;
    private final AircraftTypeRepository aircraftRepo;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        CodeshareFlightMapping mapping = mappingRepo.findAll().get(0);
        AircraftType b77w = aircraftRepo.findByIataCode("77W").orElseThrow();

        CodeshareEquipmentRule rule = new CodeshareEquipmentRule();
        rule.setFlightMapping(mapping);
        rule.setAircraftType(b77w);
        rule.setRuleType(EquipmentRuleType.ALLOWED);
        rule.setStatusCode(Status.ACTIVE);
        rule.setEffectiveFrom(LocalDate.of(2025, 1, 1));

        repository.save(rule);
    }
}