package com.codeshare.airline.ingestion.domain.context;

import com.codeshare.airline.ingestion.domain.enums.ScheduleLineIdentifier;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ScheduleGroupedMessage {

    // Grouped by type
    private final Map<ScheduleLineIdentifier, List<String>> groupedLines;

    // Original order preserved (CRITICAL for parsing)
    private final List<GenericLineClassifierContext> orderedLines;

    // Optional: raw input (useful for debugging / audit)
    private final List<String> originalLines;

    public ScheduleGroupedMessage(
            Map<ScheduleLineIdentifier, List<String>> groupedLines,
            List<GenericLineClassifierContext> orderedLines,
            List<String> originalLines
    ) {
        this.groupedLines = groupedLines;
        this.orderedLines = orderedLines;
        this.originalLines = originalLines;
    }
}