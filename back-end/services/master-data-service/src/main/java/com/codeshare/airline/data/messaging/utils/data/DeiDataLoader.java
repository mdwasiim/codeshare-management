package com.codeshare.airline.data.messaging.utils.data;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.schedule.DeiFunctionType;
import com.codeshare.airline.core.enums.schedule.DeiScopeLevel;
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
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("002", "Operating Airline Disclosure - Code Share",
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("009", "Operating Airline Disclosure - Wet Lease",
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("101", "Passenger Reservation Booking Designator Override",
                        DeiScopeLevel.SEGMENT_LEVEL, DeiFunctionType.COMMERCIAL, RecordStatus.ACTIVE),

                new DeiRegistry("125", "Joint Operation Segment Override",
                        DeiScopeLevel.SEGMENT_LEVEL, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("127", "Operating Airline Disclosure",
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, RecordStatus.ACTIVE),

                new DeiRegistry("170", "Traffic Restriction Code",
                        DeiScopeLevel.SEGMENT_LEVEL, DeiFunctionType.COMMERCIAL, RecordStatus.ACTIVE),

                new DeiRegistry("210", "Plane Change Without Aircraft Type Change",
                        DeiScopeLevel.LEG_LEVEL, DeiFunctionType.OPERATIONAL, RecordStatus.ACTIVE)
        );

        repository.saveAll(deiRegistries);
    }
}