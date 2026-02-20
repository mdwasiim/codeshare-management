package com.codeshare.airline.data.codeshare.utils.data;

import com.codeshare.airline.core.enums.codeshare.CodeshareDisclosureType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.codeshare.eitities.CodeshareFlightMapping;
import com.codeshare.airline.data.codeshare.eitities.CodeshareRoute;
import com.codeshare.airline.data.codeshare.repository.CodeshareFlightMappingRepository;
import com.codeshare.airline.data.codeshare.repository.CodeshareRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CodeshareFlightMappingLoader implements CommandLineRunner {

    private final CodeshareFlightMappingRepository repository;
    private final CodeshareRouteRepository routeRepo;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        CodeshareRoute route = routeRepo.findAll().get(0);

        CodeshareFlightMapping mapping = new CodeshareFlightMapping();
        mapping.setRoute(route);
        mapping.setMarketingFlightStart(8000);
        mapping.setMarketingFlightEnd(8009);
        mapping.setOperatingFlightStart(200);
        mapping.setOperatingFlightEnd(209);
        mapping.setDisclosureType(CodeshareDisclosureType.CODESHARE);
        mapping.setStatusCode(Status.ACTIVE);
        mapping.setEffectiveFrom(LocalDate.of(2025, 1, 1));

        repository.save(mapping);
    }
}