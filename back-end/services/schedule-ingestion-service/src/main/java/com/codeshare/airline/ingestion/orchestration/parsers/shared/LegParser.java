package com.codeshare.airline.ingestion.orchestration.parsers.shared;


import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleLegDTO;

import java.time.LocalTime;

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

        int offset = 0;

        if (value.contains("+")) {
            String[] split = value.split("\\+");
            value = split[0];
            offset = Integer.parseInt(split[1]);
        }

        if (!value.matches("^\\d{4}$")) {
            throw new IllegalArgumentException("Invalid time: " + value);
        }

        int hour = Integer.parseInt(value.substring(0, 2));
        int min  = Integer.parseInt(value.substring(2));

        if (hour > 23 || min > 59) {
            throw new IllegalArgumentException("Invalid time value: " + value);
        }

        return new TimeWithOffset(LocalTime.of(hour, min), offset);
    }

    private static class TimeWithOffset {
        LocalTime time;
        int offset;

        TimeWithOffset(LocalTime time, int offset) {
            this.time = time;
            this.offset = offset;
        }
    }


    private static LocalTime parseTime(String value) {
        return LocalTime.of(
                Integer.parseInt(value.substring(0, 2)),
                Integer.parseInt(value.substring(2))
        );
    }
}