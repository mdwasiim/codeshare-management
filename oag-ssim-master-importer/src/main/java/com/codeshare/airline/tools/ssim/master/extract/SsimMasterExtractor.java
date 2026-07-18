package com.codeshare.airline.tools.ssim.master.extract;

import com.codeshare.airline.tools.ssim.master.model.MasterCodeSet;
import com.codeshare.airline.tools.ssim.master.ssim.SsimRecordLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public final class SsimMasterExtractor {

    public MasterCodeSet extract(Path file) throws IOException {
        MasterCodeSet codes = new MasterCodeSet();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String normalized = normalizeLength(line);
                switch (normalized.charAt(0)) {
                    case '2' -> extractType2(normalized, codes);
                    case '3' -> extractType3(normalized, codes);
                    case '4' -> extractType4(normalized, codes);
                    default -> {
                    }
                }
            }
        }
        return codes;
    }

    private void extractType2(String line, MasterCodeSet codes) {
        addAirline(codes, SsimRecordLayout.T2_AIRLINE.read(line));
        add(codes.seasons, SsimRecordLayout.T2_SEASON.read(line), 2, 3);
        add(codes.timeModes, SsimRecordLayout.T2_TIME_MODE.read(line), 1, 1);
    }

    private void extractType3(String line, MasterCodeSet codes) {
        addAirline(codes, SsimRecordLayout.T3_AIRLINE.read(line));
        addAirline(codes, SsimRecordLayout.T3_ONWARD_AIRLINE.read(line));
        add(codes.operationalSuffixes, SsimRecordLayout.T3_OPERATIONAL_SUFFIX.read(line), 1, 1);
        add(codes.serviceTypes, SsimRecordLayout.T3_SERVICE_TYPE.read(line), 1, 1);
        add(codes.flightFrequencies, SsimRecordLayout.T3_OPERATING_DAYS.read(line).replace(" ", ""), 1, 7);
        addAirport(codes, SsimRecordLayout.T3_DEPARTURE_STATION.read(line));
        addAirport(codes, SsimRecordLayout.T3_ARRIVAL_STATION.read(line));
        addTerminal(codes, SsimRecordLayout.T3_DEPARTURE_STATION.read(line), SsimRecordLayout.T3_DEPARTURE_TERMINAL.read(line));
        addTerminal(codes, SsimRecordLayout.T3_ARRIVAL_STATION.read(line), SsimRecordLayout.T3_ARRIVAL_TERMINAL.read(line));
        add(codes.aircraftTypes, SsimRecordLayout.T3_AIRCRAFT_TYPE.read(line), 2, 3);
        addChars(codes.reservationBookingDesignators, SsimRecordLayout.T3_PRBD.read(line));
        addChars(codes.reservationBookingModifiers, SsimRecordLayout.T3_PRBM.read(line));
        addPairs(codes.mealServices, SsimRecordLayout.T3_MEAL_SERVICE.read(line));
        add(codes.secureFlightIndicators, SsimRecordLayout.T3_SECURE_FLIGHT.read(line), 1, 1);
        addAirline(codes, SsimRecordLayout.T3_AIRCRAFT_OWNER.read(line));
        addAirline(codes, SsimRecordLayout.T3_COCKPIT_EMPLOYER.read(line));
        addAirline(codes, SsimRecordLayout.T3_CABIN_EMPLOYER.read(line));
        add(codes.aircraftOwners, SsimRecordLayout.T3_AIRCRAFT_OWNER.read(line), 2, 3);
        add(codes.cockpitCrewEmployers, SsimRecordLayout.T3_COCKPIT_EMPLOYER.read(line), 2, 3);
        add(codes.cabinCrewEmployers, SsimRecordLayout.T3_CABIN_EMPLOYER.read(line), 2, 3);
        addChars(codes.trafficRestrictionCodes, SsimRecordLayout.T3_TRAFFIC_RESTRICTION.read(line));
    }

    private void extractType4(String line, MasterCodeSet codes) {
        addAirline(codes, SsimRecordLayout.T4_AIRLINE.read(line));
        add(codes.dataElementIdentifiers, SsimRecordLayout.T4_DEI.read(line), 1, 3);
        addAirport(codes, SsimRecordLayout.T4_BOARD_POINT.read(line));
        addAirport(codes, SsimRecordLayout.T4_OFF_POINT.read(line));
    }

    private static String normalizeLength(String line) {
        if (line.length() >= SsimRecordLayout.RECORD_LENGTH) {
            return line.substring(0, SsimRecordLayout.RECORD_LENGTH);
        }
        return line + " ".repeat(SsimRecordLayout.RECORD_LENGTH - line.length());
    }

    private static void addAirline(MasterCodeSet codes, String value) {
        add(codes.airlines, value, 2, 2);
    }

    private static void addAirport(MasterCodeSet codes, String value) {
        add(codes.airports, value, 3, 3);
    }

    private static void addTerminal(MasterCodeSet codes, String airport, String terminal) {
        String station = clean(airport);
        String code = clean(terminal);
        if (station.length() == 3 && !code.isBlank()) {
            codes.terminals.add(station + ":" + code);
        }
    }

    private static void addChars(Set<String> target, String value) {
        for (char c : value.toCharArray()) {
            if (!Character.isWhitespace(c) && c != '-' && c != '.') {
                target.add(String.valueOf(c).toUpperCase());
            }
        }
    }

    private static void addPairs(Set<String> target, String value) {
        String clean = value.replace(" ", "");
        for (int i = 0; i < clean.length(); i += 2) {
            String code = clean.substring(i, Math.min(i + 2, clean.length()));
            if (!code.isBlank() && !code.equals("--")) {
                target.add(code.toUpperCase());
            }
        }
    }

    private static void add(Set<String> target, String raw, int minLength, int maxLength) {
        String value = clean(raw);
        if (value.length() >= minLength && value.length() <= maxLength && !value.matches("[.-]+")) {
            target.add(value);
        }
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}
