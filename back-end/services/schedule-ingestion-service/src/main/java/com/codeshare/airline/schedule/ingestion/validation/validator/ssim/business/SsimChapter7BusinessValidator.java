package com.codeshare.airline.schedule.ingestion.validation.validator.ssim.business;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Order(3)
public class SsimChapter7BusinessValidator implements BusinessValidation<SsimIngestionContext> {

    private static final DateTimeFormatter SSIM_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("ddMMMyy")
            .parseDefaulting(ChronoField.ERA, 1)
            .toFormatter(Locale.ENGLISH);

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSIM);
    }

    @Override
    public ValidationResult validate(SsimIngestionContext context) {
        ValidationResult result = new ValidationResult();

        if (context == null) {
            return result;
        }

        validateDatasetLines(context.getMessageLines(), result);

        if (context.getParsedData() != null) {
            validateParsedMessage(context.getParsedData(), result);
        }

        return result;
    }

    private void validateDatasetLines(List<String> lines, ValidationResult result) {
        if (lines == null || lines.isEmpty()) {
            return;
        }

        List<String> headers = new ArrayList<>();
        List<String> carriers = new ArrayList<>();
        List<String> trailers = new ArrayList<>();

        for (String line : lines) {
            if (line == null || line.length() != 200 || line.charAt(0) == '0') {
                continue;
            }
            switch (line.charAt(0)) {
                case '1' -> headers.add(line);
                case '2' -> carriers.add(line);
                case '5' -> trailers.add(line);
                default -> {
                }
            }
        }

        if (!headers.isEmpty()) {
            validateNumberOfSeasons(headers.getFirst(), carriers, result);
        }

        for (int i = 0; i < carriers.size(); i++) {
            validateCarrierDates(carriers.get(i), result, i + 1);
            if (i < trailers.size()) {
                validateCarrierTrailerPair(carriers.get(i), trailers.get(i), result, i + 1);
            }
        }
    }

    private void validateParsedMessage(SSIMMessageDTO message, ValidationResult result) {
        String carrierCode = message.getCarrier() == null ? "" : normalize(message.getCarrier().getAirlineCode());
        Optional<LocalDate> carrierStart = message.getCarrier() == null
                ? Optional.empty()
                : parseDate(message.getCarrier().getValidityStartRaw());
        Optional<LocalDate> carrierEnd = message.getCarrier() == null
                ? Optional.empty()
                : parseDate(message.getCarrier().getValidityEndRaw());

        if (message.getFlights() == null || message.getFlights().isEmpty()) {
            return;
        }

        Map<String, List<SsimFlightDTO>> itineraries = new LinkedHashMap<>();
        for (SsimFlightDTO flight : message.getFlights()) {
            if (flight == null) {
                continue;
            }

            String recordKey = flightKey(flight);
            validateOperatingDays(flight, result, recordKey);
            validateFlightPeriod(flight, carrierStart, carrierEnd, result, recordKey);

            if (!carrierCode.isBlank() && !carrierCode.equals(normalize(flight.getAirlineCode()))) {
                result.addError("SSIM_CH7_008", "Type 3 airline must match the Carrier record airline", "TYPE_3", recordKey, ValidationStage.BUSINESS);
            }

            validateOperatingDisclosure(flight, result, recordKey);
            itineraries.computeIfAbsent(itineraryKey(flight), ignored -> new ArrayList<>()).add(flight);
        }

        for (List<SsimFlightDTO> itinerary : itineraries.values()) {
            itinerary.sort(Comparator.comparing(SsimFlightDTO::getLegSequenceNumber, Comparator.nullsLast(Integer::compareTo)));
            validateSegments(itinerary, result);
        }
    }

    private void validateNumberOfSeasons(String headerLine, List<String> carriers, ValidationResult result) {
        String rawCount = headerLine.substring(40, 41).trim();
        if (rawCount.isEmpty()) {
            return;
        }

        Set<String> seasons = new LinkedHashSet<>();
        for (String carrier : carriers) {
            String season = carrier.substring(10, 13).trim();
            if (!season.isEmpty()) {
                seasons.add(season);
            }
        }

        if (!seasons.isEmpty() && Integer.parseInt(rawCount) != seasons.size()) {
            result.addError("SSIM_CH7_001", "Type 1 number of seasons does not match Carrier record seasons", "TYPE_1", "header", ValidationStage.BUSINESS);
        }
    }

    private void validateCarrierDates(String carrierLine, ValidationResult result, int carrierIndex) {
        Optional<LocalDate> start = parseDate(carrierLine.substring(14, 21));
        Optional<LocalDate> end = parseDate(carrierLine.substring(21, 28));

        if (start.isPresent() && end.isPresent() && start.get().isAfter(end.get())) {
            result.addError("SSIM_CH7_002", "Type 2 schedule validity start date must not be after end date", "TYPE_2", "carrier:" + carrierIndex, ValidationStage.BUSINESS);
        }
    }

    private void validateCarrierTrailerPair(String carrierLine, String trailerLine, ValidationResult result, int carrierIndex) {
        String carrierAirline = normalize(carrierLine.substring(2, 5));
        String trailerAirline = normalize(trailerLine.substring(2, 5));
        if (!carrierAirline.equals(trailerAirline)) {
            result.addError("SSIM_CH7_003", "Type 5 airline must match the preceding Type 2 airline", "TYPE_5", "carrier:" + carrierIndex, ValidationStage.BUSINESS);
        }

        String carrierRelease = normalize(carrierLine.substring(64, 71));
        String trailerRelease = normalize(trailerLine.substring(5, 12));
        if (!carrierRelease.isBlank() && !trailerRelease.isBlank() && !carrierRelease.equals(trailerRelease)) {
            result.addError("SSIM_CH7_004", "Type 5 release date must match the preceding Type 2 release date", "TYPE_5", "carrier:" + carrierIndex, ValidationStage.BUSINESS);
        }
    }

    private void validateOperatingDays(SsimFlightDTO flight, ValidationResult result, String recordKey) {
        String operatingDays = normalizeRightPadded(flight.getOperatingDays(), 7);
        for (int i = 0; i < operatingDays.length(); i++) {
            char actual = operatingDays.charAt(i);
            char expected = (char) ('1' + i);
            if (actual != ' ' && actual != expected) {
                result.addError("SSIM_CH7_005", "Type 3 operating days must use canonical positions 1 through 7", "TYPE_3", recordKey, ValidationStage.BUSINESS);
                return;
            }
        }
    }

    private void validateFlightPeriod(
            SsimFlightDTO flight,
            Optional<LocalDate> carrierStart,
            Optional<LocalDate> carrierEnd,
            ValidationResult result,
            String recordKey
    ) {
        Optional<LocalDate> flightStart = parseDate(flight.getOperatingPeriodStartRaw());
        Optional<LocalDate> flightEnd = parseDate(flight.getOperatingPeriodEndRaw());

        if (flightStart.isPresent() && flightEnd.isPresent() && flightStart.get().isAfter(flightEnd.get())) {
            result.addError("SSIM_CH7_006", "Type 3 operating period start date must not be after end date", "TYPE_3", recordKey, ValidationStage.BUSINESS);
        }

        if (flightStart.isPresent() && carrierStart.isPresent() && flightStart.get().isBefore(carrierStart.get())) {
            result.addError("SSIM_CH7_007", "Type 3 operating period must fall within the Carrier record validity period", "TYPE_3", recordKey, ValidationStage.BUSINESS);
        }

        if (flightEnd.isPresent() && carrierEnd.isPresent() && flightEnd.get().isAfter(carrierEnd.get())) {
            result.addError("SSIM_CH7_007", "Type 3 operating period must fall within the Carrier record validity period", "TYPE_3", recordKey, ValidationStage.BUSINESS);
        }
    }

    private void validateOperatingDisclosure(SsimFlightDTO flight, ValidationResult result, String recordKey) {
        String disclosure = normalize(flight.getOperatingAirlineDisclosure());
        if (disclosure.isBlank()) {
            return;
        }

        boolean hasDei127 = flight.getDeis() != null && flight.getDeis().stream()
                .filter(dei -> dei != null)
                .map(SsimDataElementDTO::getDataElementIdentifier)
                .map(this::normalize)
                .anyMatch("127"::equals);

        if (("L".equals(disclosure) || "S".equals(disclosure)) && normalize(flight.getAircraftOwner()).isBlank()) {
            result.addError("SSIM_CH7_009", "Type 3 operating airline disclosure requires Aircraft Owner when code L or S is used", "TYPE_3", recordKey, ValidationStage.BUSINESS);
        }

        if (("X".equals(disclosure) || "Z".equals(disclosure)) && !hasDei127) {
            result.addError("SSIM_CH7_010", "Type 3 operating airline disclosure requires DEI 127 when code X or Z is used", "TYPE_3", recordKey, ValidationStage.BUSINESS);
        }
    }

    private void validateSegments(List<SsimFlightDTO> itinerary, ValidationResult result) {
        if (itinerary.isEmpty()) {
            return;
        }

        List<String> stations = new ArrayList<>();
        stations.add(normalize(itinerary.getFirst().getDepartureStation()));
        for (SsimFlightDTO leg : itinerary) {
            stations.add(normalize(leg.getArrivalStation()));
        }

        for (SsimFlightDTO flight : itinerary) {
            if (flight.getDeis() == null) {
                continue;
            }
            String recordKey = flightKey(flight);
            for (SsimDataElementDTO dei : flight.getDeis()) {
                if (dei == null) {
                    continue;
                }
                validateSegmentIndicator(dei, stations, result, recordKey);
            }
        }
    }

    private void validateSegmentIndicator(SsimDataElementDTO dei, List<String> stations, ValidationResult result, String recordKey) {
        int boardIndex = indicatorIndex(dei.getBoardPointIndicator());
        int offIndex = indicatorIndex(dei.getOffPointIndicator());
        String deiKey = recordKey + "/" + normalize(dei.getDataElementIdentifier());

        if (boardIndex < 0 || offIndex < 0 || boardIndex >= stations.size() || offIndex >= stations.size()) {
            result.addError("SSIM_CH7_011", "Type 4 board/off point indicators must refer to valid itinerary positions", "TYPE_4", deiKey, ValidationStage.BUSINESS);
            return;
        }

        if (boardIndex >= offIndex) {
            result.addError("SSIM_CH7_012", "Type 4 board point indicator must precede the off point indicator", "TYPE_4", deiKey, ValidationStage.BUSINESS);
        }

        String boardPoint = normalize(dei.getBoardPoint());
        String offPoint = normalize(dei.getOffPoint());
        if (!boardPoint.isBlank() && !boardPoint.equals(stations.get(boardIndex))) {
            result.addError("SSIM_CH7_013", "Type 4 board point must match the itinerary station identified by its indicator", "TYPE_4", deiKey, ValidationStage.BUSINESS);
        }
        if (!offPoint.isBlank() && !offPoint.equals(stations.get(offIndex))) {
            result.addError("SSIM_CH7_014", "Type 4 off point must match the itinerary station identified by its indicator", "TYPE_4", deiKey, ValidationStage.BUSINESS);
        }
    }

    private Optional<LocalDate> parseDate(String raw) {
        String value = normalize(raw);
        if (value.isBlank() || "00XXX00".equals(value)) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(value, SSIM_DATE_FORMATTER));
        } catch (DateTimeParseException ex) {
            return Optional.empty();
        }
    }

    private int indicatorIndex(String value) {
        String normalized = normalize(value);
        if (normalized.length() != 1) {
            return -1;
        }
        char indicator = normalized.charAt(0);
        if (indicator < 'A' || indicator > 'Z') {
            return -1;
        }
        return indicator - 'A';
    }

    private String itineraryKey(SsimFlightDTO flight) {
        return normalize(flight.getOperationalSuffix())
                + normalize(flight.getAirlineCode())
                + normalize(flight.getFlightNumber())
                + normalize(flight.getItineraryVariationIdentifier());
    }

    private String flightKey(SsimFlightDTO flight) {
        return normalize(flight.getAirlineCode())
                + normalize(flight.getFlightNumber())
                + "/" + normalize(flight.getItineraryVariationIdentifier())
                + "/" + flight.getLegSequenceNumber();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeRightPadded(String value, int length) {
        String normalized = value == null ? "" : value;
        if (normalized.length() >= length) {
            return normalized.substring(0, length);
        }
        return String.format("%-" + length + "s", normalized);
    }
}

