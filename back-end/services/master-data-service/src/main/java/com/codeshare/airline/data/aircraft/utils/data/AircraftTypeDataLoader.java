package com.codeshare.airline.data.aircraft.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.aircraft.eitities.AircraftType;
import com.codeshare.airline.data.aircraft.repository.AircraftTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AircraftTypeDataLoader implements CommandLineRunner {

    private final AircraftTypeRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<AircraftType> types = List.of(
                build("77W", "Boeing 777-300ER"),
                build("788", "Boeing 787-8"),
                build("789", "Boeing 787-9"),
                build("320", "Airbus A320"),
                build("321", "Airbus A321"),
                build("359", "Airbus A350-900"),
                build("388", "Airbus A380-800")
        );

        repository.saveAll(types);
    }

    private AircraftType build(String code, String description) {
        AircraftType type = new AircraftType();
        type.setIataCode(code);
        type.setDescription(description);
        type.setStatus(Status.ACTIVE);
        return type;
    }
}