package com.codeshare.airline.data.messaging.utils.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.schedule.DeiFunctionType;
import com.codeshare.airline.core.enums.schedule.DeiScope;
import com.codeshare.airline.data.messaging.eitities.DeiRegistry;
import com.codeshare.airline.data.messaging.repository.DeiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeiDataLoader implements CommandLineRunner {

    private final DeiRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) return;

        List<DeiRegistry> deiRegistries = List.of(
                new DeiRegistry("001", "Joint Operation Airline Designators",
                        DeiScope.FLIGHT, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("002", "Operating Airline Disclosure - Code Share",
                        DeiScope.FLIGHT, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("009", "Operating Airline Disclosure - Wet Lease",
                        DeiScope.FLIGHT, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("101", "Passenger Reservation Booking Designator Override",
                        DeiScope.SEGMENT, DeiFunctionType.COMMERCIAL, RecordStatus.ACTIVE),

                new DeiRegistry("125", "Joint Operation Segment Override",
                        DeiScope.SEGMENT, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("127", "Operating Airline Disclosure",
                        DeiScope.FLIGHT, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("170", "Traffic Restriction Code",
                        DeiScope.SEGMENT, DeiFunctionType.COMMERCIAL, RecordStatus.ACTIVE),

                new DeiRegistry("210", "Plane Change Without Aircraft Type Change",
                        DeiScope.LEG, DeiFunctionType.OPERATIONAL, RecordStatus.ACTIVE)
        );

        repository.saveAll(deiRegistries);
    }
}