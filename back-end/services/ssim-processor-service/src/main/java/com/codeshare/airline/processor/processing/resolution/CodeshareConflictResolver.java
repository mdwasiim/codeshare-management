package com.codeshare.airline.processor.resolution;

import com.codeshare.airline.processor.model.domain.FlightSchedule;

import java.util.List;
import java.util.Optional;

public interface CodeshareConflictResolver {

    Optional<FlightSchedule> resolve(
            List<FlightSchedule> candidates
    );
}
