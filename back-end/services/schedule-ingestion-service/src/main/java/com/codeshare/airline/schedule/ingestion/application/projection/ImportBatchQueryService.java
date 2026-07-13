package com.codeshare.airline.schedule.ingestion.application.projection;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportBatchDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportedScheduleDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleInstructionType;
import com.codeshare.airline.schedule.ingestion.api.response.ScheduleFileMessageResponse;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleLegDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.SchedulePeriodDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleSubMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.persistence.services.schedule.ScheduleQueryService;
import com.codeshare.airline.schedule.ingestion.persistence.services.ssim.SsimScheduleQueryService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ImportBatchQueryService {

    private static final DateTimeFormatter SSIM_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("ddMMMyy")
            .parseDefaulting(ChronoField.ERA, 1)
            .toFormatter(Locale.ENGLISH);
    private static final Pattern CODESHARE_PATTERN = Pattern.compile("([A-Z0-9]{2,3})\\s*([0-9]{1,4})([A-Z]?)");

    private final ScheduleQueryService scheduleQueryService;
    private final SsimScheduleQueryService ssimScheduleQueryService;

    public ImportBatchQueryService(
            ScheduleQueryService scheduleQueryService,
            SsimScheduleQueryService ssimScheduleQueryService
    ) {
        this.scheduleQueryService = scheduleQueryService;
        this.ssimScheduleQueryService = ssimScheduleQueryService;
    }

    public ImportBatchDTO getImportBatch(UUID importBatchId) {
        return scheduleQueryService.findParsedScheduleByImportBatchId(importBatchId)
                .map(response -> toImportBatch(response.getFile(), buildScheduleMessageImportedSchedule(response)))
                .or(() -> ssimScheduleQueryService.findFileByImportBatchId(importBatchId)
                        .flatMap(file -> ssimScheduleQueryService.findMessageByImportBatchId(importBatchId)
                                .map(message -> toImportBatch(file, buildSsimImportedSchedule(file, message)))))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Import batch not found: " + importBatchId));
    }

    public ImportedScheduleDTO getImportedSchedule(MessageType messageType, UUID importedScheduleId) {
        return switch (messageType) {
            case ASM, SSM -> buildScheduleMessageImportedSchedule(scheduleQueryService.getParsedSchedule(messageType, importedScheduleId));
            case SSIM -> buildSsimImportedSchedule(
                    ssimScheduleQueryService.getFile(importedScheduleId),
                    ssimScheduleQueryService.getMessage(importedScheduleId)
            );
        };
    }

    private ImportBatchDTO toImportBatch(ScheduleFileMetaDataDTO file, ImportedScheduleDTO importedSchedule) {
        return ImportBatchDTO.builder()
                .importBatchId(file.getLoadId())
                .importedScheduleId(file.getFileId())
                .messageType(file.getMessageType())
                .airlineCode(file.getAirlineCode())
                .sourceName(file.getFileName())
                .completedAt(firstNonNull(file.getProcessedAt(), file.getReceivedAt(), Instant.now()))
                .importedSchedule(importedSchedule)
                .build();
    }

    private ImportBatchDTO toImportBatch(SsimMetaDataDTO file, ImportedScheduleDTO importedSchedule) {
        return ImportBatchDTO.builder()
                .importBatchId(file.getLoadId())
                .importedScheduleId(file.getFileId())
                .messageType(file.getMessageType())
                .airlineCode(file.getAirlineCode())
                .sourceName(file.getFileName())
                .completedAt(firstNonNull(file.getProcessedAt(), file.getReceivedAt(), Instant.now()))
                .importedSchedule(importedSchedule)
                .build();
    }

    private ImportedScheduleDTO buildScheduleMessageImportedSchedule(ScheduleFileMessageResponse response) {
        ScheduleFileMetaDataDTO file = response.getFile();
        Map<String, ScheduleFlightSnapshotDTO> flights = new LinkedHashMap<>();

        for (ScheduleMessageDTO message : response.getMessages()) {
            if (message == null || message.getMessages() == null) {
                continue;
            }

            for (ScheduleSubMessageDTO subMessage : message.getMessages()) {
                if (subMessage == null || subMessage.getFlights() == null) {
                    continue;
                }

                for (ScheduleFlightDTO flight : subMessage.getFlights()) {
                    if (flight == null) {
                        continue;
                    }

                    String key = scheduleFlightKey(flight);
                    ScheduleFlightSnapshotDTO flightSnapshot = flights.computeIfAbsent(
                            key,
                            ignored -> ScheduleFlightSnapshotDTO.builder()
                                    .flightId(flight.getId())
                                    .airlineCode(flight.getAirlineDesignator())
                                    .flightNumber(flight.getFlightNumber())
                                    .operationalSuffix(flight.getOperationalSuffix())
                                    .itineraryVariationId(defaultItineraryVariationId())
                                    .instructionType(toInstructionType(subMessage))
                                    .messageReference(firstNonBlank(subMessage.getMessageReference(), message.getMessageReference()))
                                    .flightSequenceNumber(flight.getFlightSequenceNumber())
                                    .supplementaryInfo(new ArrayList<>(flight.getSupplementaryInfo()))
                                    .build()
                    );

                    if (flight.getDeis() != null) {
                        flight.getDeis().stream()
                                .filter(dei -> dei != null && dei.isFlightLevel())
                                .map(this::toScheduleDataElementSnapshot)
                                .forEach(flightSnapshot.getFlightDataElements()::add);
                    }

                    List<SchedulePeriodDTO> periods = flight.getPeriods();
                    if (periods == null || periods.isEmpty()) {
                        periods = List.of(new SchedulePeriodDTO());
                    }

                    for (SchedulePeriodDTO period : periods) {
                        if (flight.getLegs() == null || flight.getLegs().isEmpty()) {
                            continue;
                        }

                        for (ScheduleLegDTO leg : flight.getLegs()) {
                            flightSnapshot.getLegs().add(toScheduleLegSnapshot(flight, leg, period));
                        }
                    }
                }
            }
        }

        return ImportedScheduleDTO.builder()
                .importedScheduleId(file.getFileId())
                .importBatchId(file.getLoadId())
                .messageType(file.getMessageType())
                .airlineCode(file.getAirlineCode())
                .sourceName(file.getFileName())
                .importedAt(firstNonNull(file.getProcessedAt(), file.getReceivedAt(), Instant.now()))
                .fullSchedule(file.getMessageType() == MessageType.SSIM)
                .flights(new ArrayList<>(flights.values()))
                .build();
    }

    private ImportedScheduleDTO buildSsimImportedSchedule(SsimMetaDataDTO file, SSIMMessageDTO message) {
        Map<String, List<SsimFlightDTO>> groupedFlights = new LinkedHashMap<>();

        if (message.getFlights() != null) {
            for (SsimFlightDTO flight : message.getFlights()) {
                if (flight == null) {
                    continue;
                }
                groupedFlights.computeIfAbsent(ssimFlightKey(flight), ignored -> new ArrayList<>()).add(flight);
            }
        }

        List<ScheduleFlightSnapshotDTO> flights = new ArrayList<>();
        for (List<SsimFlightDTO> grouped : groupedFlights.values()) {
            grouped.sort(Comparator.comparing(SsimFlightDTO::getLegSequenceNumber, Comparator.nullsLast(Integer::compareTo)));
            SsimFlightDTO first = grouped.getFirst();

            ScheduleFlightSnapshotDTO flightSnapshot = ScheduleFlightSnapshotDTO.builder()
                    .flightId(first.getId())
                    .airlineCode(first.getAirlineCode())
                    .flightNumber(first.getFlightNumber())
                    .operationalSuffix(first.getOperationalSuffix())
                    .itineraryVariationId(defaultIfBlank(first.getItineraryVariationIdentifier(), defaultItineraryVariationId()))
                    .instructionType(ScheduleInstructionType.BASELINE_LOAD)
                    .flightSequenceNumber(first.getLegSequenceNumber())
                    .messageReference(message.getCarrier() != null ? message.getCarrier().getRecordSerialNumber() : null)
                    .build();

            for (SsimFlightDTO leg : grouped) {
                flightSnapshot.getLegs().add(toSsimLegSnapshot(leg));
            }

            flights.add(flightSnapshot);
        }

        return ImportedScheduleDTO.builder()
                .importedScheduleId(file.getFileId())
                .importBatchId(file.getLoadId())
                .messageType(MessageType.SSIM)
                .airlineCode(file.getAirlineCode())
                .sourceName(file.getFileName())
                .importedAt(firstNonNull(file.getProcessedAt(), file.getReceivedAt(), Instant.now()))
                .fullSchedule(true)
                .flights(flights)
                .build();
    }

    private ScheduleLegSnapshotDTO toScheduleLegSnapshot(
            ScheduleFlightDTO flight,
            ScheduleLegDTO leg,
            SchedulePeriodDTO period
    ) {
        ScheduleLegSnapshotDTO snapshot = ScheduleLegSnapshotDTO.builder()
                .legId(leg.getId())
                .legSequenceNumber(leg.getLegSequenceNumber())
                .periodStart(period.getStartDate())
                .periodEnd(period.getEndDate())
                .daysOfOperation(period.getDaysOfOperation())
                .frequencyRate(period.getFrequencyRate() == null ? null : String.valueOf(period.getFrequencyRate()))
                .departureStation(leg.getBoardPoint())
                .arrivalStation(leg.getOffPoint())
                .scheduledDepartureTime(leg.getDepartureTime())
                .aircraftDepartureTime(leg.getDepartureTime())
                .aircraftArrivalTime(leg.getArrivalTime())
                .scheduledArrivalTime(leg.getArrivalTime())
                .dateVariation(toDateVariation(leg.getArrivalDayOffset()))
                .aircraftType(firstNonBlank(leg.getAircraftType(), flight.getAircraftType()))
                .aircraftConfiguration(firstNonBlank(leg.getAircraftConfiguration(), flight.getAircraftConfiguration()))
                .serviceType(firstNonBlank(leg.getServiceType(), flight.getServiceType()))
                .bookingDesignator(flight.getBookingDesignator())
                .build();

        List<ScheduleDataElementDTO> segmentDeis = new ArrayList<>();
        if (leg.getDeis() != null) {
            for (ScheduleDataElementDTO dei : leg.getDeis()) {
                if (dei == null) {
                    continue;
                }
                if (dei.isSegmentLevel()) {
                    segmentDeis.add(dei);
                } else {
                    snapshot.getLegDataElements().add(toScheduleDataElementSnapshot(dei));
                }
            }
        }

        if (flight.getDeis() != null) {
            for (ScheduleDataElementDTO dei : flight.getDeis()) {
                if (dei != null && dei.isSegmentLevel()) {
                    segmentDeis.add(dei);
                }
            }
        }

        populateSegmentsAndCodeshares(snapshot, segmentDeis);
        return snapshot;
    }

    private ScheduleLegSnapshotDTO toSsimLegSnapshot(SsimFlightDTO flight) {
        ScheduleLegSnapshotDTO snapshot = ScheduleLegSnapshotDTO.builder()
                .legId(flight.getId())
                .legSequenceNumber(flight.getLegSequenceNumber())
                .periodStart(parseSsimDate(flight.getOperatingPeriodStartRaw()).orElse(null))
                .periodEnd(parseSsimDate(flight.getOperatingPeriodEndRaw()).orElse(null))
                .daysOfOperation(normalizeDays(flight.getOperatingDays()))
                .frequencyRate(blankToNull(flight.getFrequencyRate()))
                .departureStation(flight.getDepartureStation())
                .arrivalStation(flight.getArrivalStation())
                .scheduledDepartureTime(parseSsimTime(flight.getPassengerStd()))
                .aircraftDepartureTime(parseSsimTime(flight.getAircraftStd()))
                .departureUtcOffset(blankToNull(flight.getDepartureUtcVariation()))
                .departureTerminal(blankToNull(flight.getDepartureTerminal()))
                .aircraftArrivalTime(parseSsimTime(flight.getAircraftSta()))
                .scheduledArrivalTime(parseSsimTime(flight.getPassengerSta()))
                .arrivalUtcOffset(blankToNull(flight.getArrivalUtcVariation()))
                .arrivalTerminal(blankToNull(flight.getArrivalTerminal()))
                .dateVariation(blankToNull(flight.getDateVariation()))
                .aircraftType(blankToNull(flight.getAircraftType()))
                .aircraftConfiguration(blankToNull(flight.getAircraftConfigurationVersion()))
                .serviceType(blankToNull(flight.getServiceType()))
                .bookingDesignator(blankToNull(flight.getPassengerReservationBookingDesignator()))
                .bookingModifier(blankToNull(flight.getPassengerReservationBookingModifier()))
                .mealServiceNote(blankToNull(flight.getMealServiceNote()))
                .jointOperationAirlines(blankToNull(flight.getJointOperationAirlineDesignators()))
                .minimumConnectingTimeStatus(blankToNull(flight.getMinimumConnectingTimeStatus()))
                .secureFlightIndicator(blankToNull(flight.getSecureFlightIndicator()))
                .aircraftOwner(blankToNull(flight.getAircraftOwner()))
                .cockpitCrewEmployer(blankToNull(flight.getCockpitCrewEmployer()))
                .cabinCrewEmployer(blankToNull(flight.getCabinCrewEmployer()))
                .onwardAirlineDesignator(blankToNull(flight.getOnwardAirlineDesignator()))
                .onwardFlightNumber(blankToNull(flight.getOnwardFlightNumber()))
                .onwardOperationalSuffix(blankToNull(flight.getOnwardOperationalSuffix()))
                .aircraftRotationLayover(blankToNull(flight.getAircraftRotationLayover()))
                .flightTransitLayover(blankToNull(flight.getFlightTransitLayover()))
                .operatingAirlineDisclosure(blankToNull(flight.getOperatingAirlineDisclosure()))
                .trafficRestrictionCode(blankToNull(flight.getTrafficRestrictionCode()))
                .build();

        populateSsimSegmentsAndCodeshares(snapshot, flight.getDeis());
        return snapshot;
    }

    private void populateSegmentsAndCodeshares(
            ScheduleLegSnapshotDTO legSnapshot,
            List<ScheduleDataElementDTO> segmentDeis
    ) {
        Map<String, ScheduleSegmentSnapshotDTO> segments = new LinkedHashMap<>();
        Map<String, ScheduleCodeshareSnapshotDTO> codeshares = new LinkedHashMap<>();

        for (ScheduleDataElementDTO dei : segmentDeis) {
            if (dei == null) {
                continue;
            }

            String code = formatDeiCode(dei.getDeiCode());
            Optional<ScheduleCodeshareSnapshotDTO> codeshare = extractCodeshare(
                    code,
                    dei.getValue(),
                    dei.getBoardPoint(),
                    dei.getOffPoint(),
                    dei.getSequenceOrder(),
                    codeshares
            );

            if (codeshare.isPresent()) {
                codeshares.put(codeshareKey(codeshare.get()), codeshare.get());
                continue;
            }

            String segmentKey = segmentKey(dei.getBoardPoint(), dei.getOffPoint());
            ScheduleSegmentSnapshotDTO segment = segments.computeIfAbsent(
                    segmentKey,
                    ignored -> ScheduleSegmentSnapshotDTO.builder()
                            .boardPoint(blankToNull(dei.getBoardPoint()))
                            .offPoint(blankToNull(dei.getOffPoint()))
                            .build()
            );
            segment.getDataElements().add(toScheduleDataElementSnapshot(dei));
        }

        legSnapshot.getSegments().addAll(segments.values());
        legSnapshot.getCodeshares().addAll(codeshares.values());
    }

    private void populateSsimSegmentsAndCodeshares(
            ScheduleLegSnapshotDTO legSnapshot,
            List<SsimDataElementDTO> deis
    ) {
        if (deis == null) {
            return;
        }

        Map<String, ScheduleSegmentSnapshotDTO> segments = new LinkedHashMap<>();
        Map<String, ScheduleCodeshareSnapshotDTO> codeshares = new LinkedHashMap<>();
        int sequence = 1;

        for (SsimDataElementDTO dei : deis) {
            if (dei == null) {
                continue;
            }

            String boardPoint = blankToNull(dei.getBoardPoint());
            String offPoint = blankToNull(dei.getOffPoint());
            String deiCode = blankToNull(dei.getDataElementIdentifier());
            String value = blankToNull(dei.getDeiData());

            if (hasText(boardPoint) && hasText(offPoint)) {
                Optional<ScheduleCodeshareSnapshotDTO> codeshare = extractCodeshare(
                        deiCode,
                        value,
                        boardPoint,
                        offPoint,
                        sequence,
                        codeshares
                );
                if (codeshare.isPresent()) {
                    codeshares.put(codeshareKey(codeshare.get()), codeshare.get());
                    sequence++;
                    continue;
                }

                String segmentKey = segmentKey(boardPoint, offPoint);
                ScheduleSegmentSnapshotDTO segment = segments.computeIfAbsent(
                        segmentKey,
                        ignored -> ScheduleSegmentSnapshotDTO.builder()
                                .boardPoint(boardPoint)
                                .offPoint(offPoint)
                                .build()
                );
                segment.getDataElements().add(ScheduleDataElementSnapshotDTO.builder()
                        .dataElementId(dei.getId())
                        .code(deiCode)
                        .value(value)
                        .scope("SEGMENT")
                        .sequenceOrder(sequence)
                        .boardPoint(boardPoint)
                        .offPoint(offPoint)
                        .build());
            } else {
                legSnapshot.getLegDataElements().add(ScheduleDataElementSnapshotDTO.builder()
                        .dataElementId(dei.getId())
                        .code(deiCode)
                        .value(value)
                        .scope("LEG")
                        .sequenceOrder(sequence)
                        .build());
            }
            sequence++;
        }

        legSnapshot.getSegments().addAll(segments.values());
        legSnapshot.getCodeshares().addAll(codeshares.values());
    }

    private ScheduleDataElementSnapshotDTO toScheduleDataElementSnapshot(ScheduleDataElementDTO dei) {
        return ScheduleDataElementSnapshotDTO.builder()
                .dataElementId(dei.getId())
                .code(formatDeiCode(dei.getDeiCode()))
                .value(blankToNull(dei.getValue()))
                .scope(dei.getScope() == null ? null : dei.getScope().name())
                .sequenceOrder(dei.getSequenceOrder())
                .boardPoint(blankToNull(dei.getBoardPoint()))
                .offPoint(blankToNull(dei.getOffPoint()))
                .build();
    }

    private Optional<ScheduleCodeshareSnapshotDTO> extractCodeshare(
            String code,
            String value,
            String boardPoint,
            String offPoint,
            Integer sequenceOrder,
            Map<String, ScheduleCodeshareSnapshotDTO> currentCodeshares
    ) {
        if (!"010".equals(code) && !"011".equals(code)) {
            return Optional.empty();
        }

        if ("011".equals(code) && !currentCodeshares.isEmpty()) {
            ScheduleCodeshareSnapshotDTO existing = currentCodeshares.values().stream()
                    .filter(snapshot -> equalsOrBlank(snapshot.getBoardPoint(), boardPoint)
                            && equalsOrBlank(snapshot.getOffPoint(), offPoint))
                    .reduce((first, second) -> second)
                    .orElse(null);
            if (existing != null) {
                existing.setMarketingBookingDesignator(value);
                return Optional.of(existing);
            }
        }

        Matcher matcher = CODESHARE_PATTERN.matcher(normalizeAlphanumeric(value));
        if (!matcher.find()) {
            return Optional.empty();
        }

        ScheduleCodeshareSnapshotDTO snapshot = ScheduleCodeshareSnapshotDTO.builder()
                .sequenceOrder(sequenceOrder == null ? 1 : sequenceOrder)
                .marketingAirlineCode(matcher.group(1))
                .marketingFlightNumber(leftPadFlightNumber(matcher.group(2)))
                .marketingOperationalSuffix(blankToNull(matcher.group(3)))
                .boardPoint(blankToNull(boardPoint))
                .offPoint(blankToNull(offPoint))
                .marketingBookingDesignator("011".equals(code) ? value : null)
                .sourceDeiCode(code)
                .codeshare(true)
                .build();
        return Optional.of(snapshot);
    }

    private ScheduleInstructionType toInstructionType(ScheduleSubMessageDTO subMessage) {
        if (subMessage == null || subMessage.getActionType() == null) {
            return ScheduleInstructionType.UNKNOWN;
        }

        return switch (subMessage.getActionType()) {
            case CREATE -> ScheduleInstructionType.CREATE;
            case UPDATE -> ScheduleInstructionType.UPDATE;
            case DELETE -> ScheduleInstructionType.DELETE;
            case REPLACE -> ScheduleInstructionType.REPLACE;
            case SCHEDULE_CHANGE -> ScheduleInstructionType.SCHEDULE_CHANGE;
            case REVISION -> ScheduleInstructionType.REVISION;
            case REQUEST_SCHEDULE_DATA -> ScheduleInstructionType.REQUEST_SCHEDULE_DATA;
            case IDENTIFIER_CHANGE -> ScheduleInstructionType.IDENTIFIER_CHANGE;
            case TIME_CHANGE -> ScheduleInstructionType.TIME_CHANGE;
            case ROUTING_CHANGE -> ScheduleInstructionType.ROUTING_CHANGE;
            case EQUIPMENT_CHANGE -> ScheduleInstructionType.EQUIPMENT_CHANGE;
            case CONFIGURATION_CHANGE -> ScheduleInstructionType.CONFIGURATION_CHANGE;
            case REINSTATE -> ScheduleInstructionType.REINSTATE;
            case ACKNOWLEDGED -> ScheduleInstructionType.ACKNOWLEDGED;
            case NOT_ACKNOWLEDGED -> ScheduleInstructionType.NOT_ACKNOWLEDGED;
            case ADMINISTRATIVE -> ScheduleInstructionType.ADMINISTRATIVE;
            case BASELINE_LOAD -> ScheduleInstructionType.BASELINE_LOAD;
            case UNKNOWN -> ScheduleInstructionType.UNKNOWN;
        };
    }

    private Optional<LocalDate> parseSsimDate(String raw) {
        String value = blankToNull(raw);
        if (value == null || "00XXX00".equals(value)) {
            return Optional.empty();
        }

        try {
            return Optional.of(LocalDate.parse(value, SSIM_DATE_FORMATTER));
        } catch (DateTimeParseException ex) {
            return Optional.empty();
        }
    }

    private LocalTime parseSsimTime(String raw) {
        String value = blankToNull(raw);
        if (value == null || value.length() != 4 || !value.chars().allMatch(Character::isDigit)) {
            return null;
        }
        if ("2400".equals(value)) {
            return LocalTime.MIDNIGHT;
        }
        return LocalTime.of(Integer.parseInt(value.substring(0, 2)), Integer.parseInt(value.substring(2, 4)));
    }

    private String scheduleFlightKey(ScheduleFlightDTO flight) {
        return defaultIfBlank(flight.getAirlineDesignator(), "")
                + "|"
                + defaultIfBlank(flight.getFlightNumber(), "")
                + "|"
                + defaultIfBlank(flight.getOperationalSuffix(), "")
                + "|"
                + defaultItineraryVariationId();
    }

    private String ssimFlightKey(SsimFlightDTO flight) {
        return defaultIfBlank(flight.getAirlineCode(), "")
                + "|"
                + defaultIfBlank(flight.getFlightNumber(), "")
                + "|"
                + defaultIfBlank(flight.getOperationalSuffix(), "")
                + "|"
                + defaultIfBlank(flight.getItineraryVariationIdentifier(), defaultItineraryVariationId());
    }

    private String segmentKey(String boardPoint, String offPoint) {
        return defaultIfBlank(boardPoint, "") + "|" + defaultIfBlank(offPoint, "");
    }

    private String codeshareKey(ScheduleCodeshareSnapshotDTO snapshot) {
        return defaultIfBlank(snapshot.getMarketingAirlineCode(), "")
                + "|"
                + defaultIfBlank(snapshot.getMarketingFlightNumber(), "")
                + "|"
                + defaultIfBlank(snapshot.getBoardPoint(), "")
                + "|"
                + defaultIfBlank(snapshot.getOffPoint(), "")
                + "|"
                + snapshot.getSequenceOrder();
    }

    private String formatDeiCode(Integer code) {
        if (code == null) {
            return null;
        }
        return String.format(Locale.ENGLISH, "%03d", code);
    }

    private String toDateVariation(Integer dayOffset) {
        if (dayOffset == null || dayOffset == 0) {
            return null;
        }
        return "+" + dayOffset;
    }

    private String normalizeDays(String operatingDays) {
        return blankToNull(operatingDays) == null ? null : operatingDays.replace(" ", "");
    }

    private String normalizeAlphanumeric(String value) {
        return value == null ? "" : value.replace("/", " ").trim().toUpperCase(Locale.ENGLISH);
    }

    private String leftPadFlightNumber(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return String.format(Locale.ENGLISH, "%04d", Integer.parseInt(value));
    }

    private boolean equalsOrBlank(String left, String right) {
        return !hasText(left) || !hasText(right) || left.equalsIgnoreCase(right);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String blankToNull(String value) {
        return hasText(value) ? value.trim() : null;
    }

    private String defaultIfBlank(String value, String defaultValue) {
        return hasText(value) ? value.trim() : defaultValue;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value.trim();
            }
        }
        return null;
    }

    @SafeVarargs
    private <T> T firstNonNull(T... values) {
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private String defaultItineraryVariationId() {
        return "00";
    }
}

