package com.codeshare.airline.data.ssim.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.core.enums.schedule.DeiFunctionType;
import com.codeshare.airline.core.enums.schedule.DeiScopeLevel;
import com.codeshare.airline.data.ssim.eitities.Dei;
import com.codeshare.airline.data.ssim.repository.DeiRepository;
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

        List<Dei> deis = List.of(
                new Dei("001", "Joint Operation Airline Designators",
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, Status.ACTIVE),

                new Dei("002", "Operating Airline Disclosure - Code Share",
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, Status.ACTIVE),

                new Dei("009", "Operating Airline Disclosure - Wet Lease",
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, Status.ACTIVE),

                new Dei("101", "Passenger Reservation Booking Designator Override",
                        DeiScopeLevel.SEGMENT_LEVEL, DeiFunctionType.COMMERCIAL, Status.ACTIVE),

                new Dei("125", "Joint Operation Segment Override",
                        DeiScopeLevel.SEGMENT_LEVEL, DeiFunctionType.DISCLOSURE, Status.ACTIVE),

                new Dei("127", "Operating Airline Disclosure",
                        DeiScopeLevel.FLIGHT_LEVEL, DeiFunctionType.DISCLOSURE, Status.ACTIVE),

                new Dei("170", "Traffic Restriction Code",
                        DeiScopeLevel.SEGMENT_LEVEL, DeiFunctionType.COMMERCIAL, Status.ACTIVE),

                new Dei("210", "Plane Change Without Aircraft Type Change",
                        DeiScopeLevel.LEG_LEVEL, DeiFunctionType.OPERATIONAL, Status.ACTIVE)
        );

        repository.saveAll(deis);
    }
}