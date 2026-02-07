package com.codeshare.airline.processor.expansion.repository;

import com.codeshare.airline.processor.model.domain.FlightSchedule;
import com.codeshare.airline.processor.model.raw.SsimR3FlightLegRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, UUID> {

    List<FlightSchedule> findBySourceLeg(SsimR3FlightLegRecord leg);

    List<FlightSchedule> findBySourceLegAndCodeshareFalse(SsimR3FlightLegRecord leg);
}
