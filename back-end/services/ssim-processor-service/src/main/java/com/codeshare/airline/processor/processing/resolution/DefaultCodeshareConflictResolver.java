package com.codeshare.airline.processor.processing.resolution;

import com.codeshare.airline.processor.model.domain.FlightSchedule;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultCodeshareConflictResolver implements CodeshareConflictResolver {

    @Override
    public Optional<FlightSchedule> resolve(List<FlightSchedule> candidates) {

        // Layer 3 — Own metal
        Optional<FlightSchedule> ownMetal =
                candidates.stream()
                        .filter(fs ->
                                fs.getCarrier()
                                        .equals(fs.getOperatingCarrier()))
                        .findFirst();

        if (ownMetal.isPresent()) {
            return ownMetal;
        }

        // Layer 2 — Priority
        int minPriority =
                candidates.stream()
                        .mapToInt(FlightSchedule::getCodesharePriority)
                        .min()
                        .orElse(Integer.MAX_VALUE);

        List<FlightSchedule> priorityWinners =
                candidates.stream()
                        .filter(fs -> fs.getCodesharePriority() == minPriority)
                        .toList();

        if (priorityWinners.size() == 1) {
            return Optional.of(priorityWinners.get(0));
        }

        // Layer 4 — Earliest departure
        return priorityWinners.stream()
                .min(Comparator.comparing(
                        FlightSchedule::getScheduledDepartureTime
                ));
    }
}
