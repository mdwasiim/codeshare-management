package com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared;


import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleLegDTO;

public class LegParser {

    public static ScheduleLegDTO parse(String line, int seq) {

        String[] parts = line.trim().split("\\s+");

        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid LEG line: " + line);
        }

        String origin = parts[0];
        String destination = parts[1];

        if (!origin.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Invalid origin: " + origin);
        }

        if (!destination.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Invalid destination: " + destination);
        }

        ScheduleLegDTO leg = new ScheduleLegDTO();

        leg.setLegSequenceNumber(seq);
        leg.setBoardPoint(origin);
        leg.setOffPoint(destination);

        /* ================= TIME PARSING ================= */

        if (parts.length >= 4) {

            TimeWithOffset dep = parseTimeWithOffset(parts[2]);
            TimeWithOffset arr = parseTimeWithOffset(parts[3]);

            leg.setDepartureTime(dep.time);
            leg.setArrivalTime(arr.time);
            leg.setDepartureDayOffset(dep.offset);
            leg.setArrivalDayOffset(arr.offset);
        }

        return leg;
    }

    private static TimeWithOffset parseTimeWithOffset(String value) {
        TimeParser.ParsedSingleTime parsed = TimeParser.parseToken(value);
        return new TimeWithOffset(parsed.getTime(), parsed.getOffset());
    }

    private static class TimeWithOffset {
        java.time.LocalTime time;
        int offset;

        TimeWithOffset(java.time.LocalTime time, int offset) {
            this.time = time;
            this.offset = offset;
        }
    }
}
