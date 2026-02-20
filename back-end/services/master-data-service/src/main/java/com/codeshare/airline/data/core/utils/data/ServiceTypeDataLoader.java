package com.codeshare.airline.data.core.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.ServiceType;
import com.codeshare.airline.data.core.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ServiceTypeDataLoader {

    private final ServiceTypeRepository repository;

    @PostConstruct
    public void load() {

        if (repository.count() > 0) return;

        ServiceType scheduledPassenger = new ServiceType();
        scheduledPassenger.setServiceTypeCode("J");
        scheduledPassenger.setServiceTypeName("Scheduled Passenger");
        scheduledPassenger.setDescription("Regular scheduled passenger service");
        scheduledPassenger.setStatusCode(Status.ACTIVE);
        scheduledPassenger.setEffectiveFrom(LocalDate.now());

        ServiceType additionalPassenger = new ServiceType();
        additionalPassenger.setServiceTypeCode("G");
        additionalPassenger.setServiceTypeName("Additional Passenger");
        additionalPassenger.setDescription("Additional passenger service");
        additionalPassenger.setStatusCode(Status.ACTIVE);
        additionalPassenger.setEffectiveFrom(LocalDate.now());

        ServiceType cargo = new ServiceType();
        cargo.setServiceTypeCode("F");
        cargo.setServiceTypeName("Cargo");
        cargo.setDescription("Scheduled cargo service");
        cargo.setStatusCode(Status.ACTIVE);
        cargo.setEffectiveFrom(LocalDate.now());

        repository.save(scheduledPassenger);
        repository.save(additionalPassenger);
        repository.save(cargo);
    }
}