package com.codeshare.airline.data.codeshare.utils.data;

import com.codeshare.airline.core.enums.codeshare.CodeshareRouteScopeType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.codeshare.eitities.CodeshareAgreement;
import com.codeshare.airline.data.codeshare.eitities.CodeshareRoute;
import com.codeshare.airline.data.codeshare.repository.CodeshareAgreementRepository;
import com.codeshare.airline.data.codeshare.repository.CodeshareRouteRepository;
import com.codeshare.airline.data.core.eitities.Airport;
import com.codeshare.airline.data.core.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CodeshareRouteLoader implements CommandLineRunner {

    private final CodeshareRouteRepository repository;
    private final CodeshareAgreementRepository agreementRepo;
    private final AirportRepository airportRepo;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        CodeshareAgreement agreement = agreementRepo.findByAgreementCode("QR-BA-INT-2025").orElseThrow();
        Airport doh = airportRepo.findByIataCode("DOH").orElseThrow();
        Airport lhr = airportRepo.findByIataCode("LHR").orElseThrow();

        CodeshareRoute route = new CodeshareRoute();
        route.setAgreement(agreement);
        route.setOrigin(doh);
        route.setDestination(lhr);
        route.setBidirectional(false);
        route.setRouteScopeType(CodeshareRouteScopeType.NON_STOP_ONLY);
        route.setStatusCode(Status.ACTIVE);
        route.setEffectiveFrom(LocalDate.of(2025, 1, 1));

        repository.save(route);
    }
}