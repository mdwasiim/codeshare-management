package com.codeshare.airline.data.codeshare.utils.data;

import com.codeshare.airline.core.enums.codeshare.CodeshareDisclosureType;
import com.codeshare.airline.core.enums.codeshare.CodeshareInventoryType;
import com.codeshare.airline.core.enums.codeshare.CodeshareScopeType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.data.core.repository.AirlineCarrierRepository;
import com.codeshare.airline.data.codeshare.eitities.CodeshareAgreement;
import com.codeshare.airline.data.codeshare.repository.CodeshareAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CodeshareAgreementLoader implements CommandLineRunner {

    private final CodeshareAgreementRepository repository;
    private final AirlineCarrierRepository airlineRepo;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        AirlineCarrier qr = airlineRepo.findByAirlineCode("QR").orElseThrow();
        AirlineCarrier ba = airlineRepo.findByAirlineCode("BA").orElseThrow();

        CodeshareAgreement agreement = new CodeshareAgreement();
        agreement.setAgreementCode("QR-BA-INT-2025");
        agreement.setMarketingAirline(qr);
        agreement.setOperatingAirline(ba);
        agreement.setDisclosureType(CodeshareDisclosureType.CODESHARE);
        agreement.setScopeType(CodeshareScopeType.ROUTE_SPECIFIC);
        agreement.setInventoryType(CodeshareInventoryType.FREE_SALE);
        agreement.setStatusCode(Status.ACTIVE);
        agreement.setEffectiveFrom(LocalDate.of(2025, 1, 1));

        repository.save(agreement);
    }
}