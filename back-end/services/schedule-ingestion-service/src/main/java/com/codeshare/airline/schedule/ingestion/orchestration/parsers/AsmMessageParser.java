package com.codeshare.airline.schedule.ingestion.orchestration.parsers;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifier;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifierFactory;
import com.codeshare.airline.schedule.ingestion.shared.util.LineClassifierUtil;
import com.codeshare.airline.schedule.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import com.codeshare.airline.schedule.ingestion.dto.schedule.*;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared.*;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule.ActionTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AsmMessageParser extends AbstractScheduleParser<ScheduleMessageDTO> {

    /* ================= STATE ================= */

    protected int globalDeiSeq;
    protected ScheduleMessageDTO envelope;
    protected ScheduleSubMessageDTO message;
    protected ScheduleFlightDTO flight;
    protected ScheduleLegDTO currentLeg;
    protected TimeMode pendingTimeMode;

    /* ================= INIT ================= */

    @Override
    protected void init() {
        globalDeiSeq = 1;
        envelope = new ScheduleMessageDTO();
        message = null;
        flight = null;
        currentLeg = null;
        pendingTimeMode = null;
    }

    @Override
    protected LineClassifier classifier() {
        return LineClassifierFactory.create(MessageType.ASM);
    }

    /* ================= MAIN ================= */

    @Override
    protected void handleLine(GenericLineClassifierContext ctx) {

        String line = ctx.getNormalizedLine();

        // optional raw tracking
        if (message != null) {
            if (message.getRawLines() == null) {
                message.setRawLines(new ArrayList<>());
            }
            message.getRawLines().add(line);
        }

        switch (ctx.getType()) {

            case HEADER -> {
                try {
                    envelope.setMessageType(MessageType.valueOf(line));
                } catch (Exception e) {
                    log.warn("Invalid message type: {}", line);
                }
            }

            case TIME_MODE -> pendingTimeMode = TimeMode.valueOf(line);

            case MESSAGE_REFERENCE -> envelope.setRawHeader(line);

            case ACTION -> handleAction(line);

            case FLIGHT -> handleFlight(line);

            case LEG -> handleLeg(line);

            case TIME -> handleTime(line);

            case EQUIPMENT_AND_SERVICE -> handleEquipment(line);

            case DEI -> handleDei(line, flight, currentLeg, globalDeiSeq++);

            case SUPPLEMENTARY -> handleSupplementary(line);

            case SUB_MESSAGE_SEPARATOR -> resetContext();

            case PERIOD -> log.warn("Ignoring PERIOD in ASM: {}", line);

            default -> log.debug("Ignoring line: {}", line);
        }
    }

    /* ================= ACTION ================= */

    private void handleAction(String line) {

        message = new ScheduleSubMessageDTO();
        envelope.addMessage(message);

        String token = extractFirstToken(line);
        message.setActionType(ActionTypeMapper.fromAsm(token));

        message.setTimeMode(pendingTimeMode != null ? pendingTimeMode : TimeMode.UTC);
        pendingTimeMode = null; // ✅ FIXED

        message.setRawLines(new ArrayList<>());

        flight = null;
        currentLeg = null;
    }

    /* ================= FLIGHT ================= */

    private void handleFlight(String line) {

        if (message == null) {
            throw new IllegalStateException("FLIGHT before ACTION: " + line);
        }

        flight = new ScheduleFlightDTO();
        message.addFlight(flight);

        var identity = FlightIdentityParser.parseAsm(line);

        flight.setAirlineDesignator(identity.getAirlineDesignator());
        flight.setFlightNumber(identity.getFlightNumber());
        flight.setOperationalSuffix(identity.getOperationalSuffix());
        flight.setBoardPoint(identity.getBoardPoint());
        flight.setOffPoint(identity.getOffPoint());
        flight.setOperationDate(identity.getOperationDate());

        currentLeg = null;
    }

    /* ================= LEG ================= */

    private void handleLeg(String line) {

        if (flight == null) {
            throw new IllegalStateException("LEG before FLIGHT: " + line);
        }

        applyRoutingChangeIfNeeded();

        String[] parts = line.trim().split("\\s+");

        // 1️⃣ Parse LEG (first 2 tokens ONLY)
        String legPart = parts[0] + " " + parts[1];

        ScheduleLegDTO leg = LegParser.parse(legPart, flight.getLegs().size() + 1);

        // 2️⃣ 🔥 Handle TIME inside same line (COMPOSITE CASE)
        if (parts.length >= 4) {

            String timeCandidate = parts[2] + " " + parts[3];

            if (LineClassifierUtil.isTime(timeCandidate)) {

                var times = TimeParser.parse(timeCandidate);

                if (times != null) {
                    leg.setDepartureTime(times.getStd());
                    leg.setArrivalTime(times.getSta());
                    leg.setDepartureDayOffset(times.getDepOffset());
                    leg.setArrivalDayOffset(times.getArrOffset());
                }
            }
        }

        flight.addLeg(leg);
        currentLeg = leg;
    }

    /* ================= TIME ================= */

    private void handleTime(String line) {

        if (message == null || currentLeg == null) {
            log.warn("TIME without context: {}", line);
            return;
        }

        if (message.getActionType() != ActionType.TIME_CHANGE) {
            return; // 🔥 important
        }

        var times = TimeParser.parse(line);

        if (times == null) {
            log.warn("Invalid TIME line: {}", line);
            return; // 🔥 STOP — no crash
        }

        currentLeg.setDepartureTime(times.getStd());
        currentLeg.setArrivalTime(times.getSta());
        currentLeg.setDepartureDayOffset(times.getDepOffset());
        currentLeg.setArrivalDayOffset(times.getArrOffset());
    }

    /* ================= EQUIPMENT ================= */

    private void handleEquipment(String line) {

        if (flight == null) {
            throw new IllegalStateException("EQUIPMENT before FLIGHT: " + line);
        }

        var eq = EquipmentParser.parse(line);

        if (eq == null) {
            log.warn("Invalid EQUIPMENT: {}", line);
            return;
        }

        if (currentLeg != null) {
            currentLeg.setAircraftType(eq.getAircraftType());
            currentLeg.setAircraftConfiguration(eq.getAircraftConfiguration());
            currentLeg.setServiceType(eq.getServiceType());
        } else {
            flight.setAircraftType(eq.getAircraftType());
            flight.setAircraftConfiguration(eq.getAircraftConfiguration());
            flight.setServiceType(eq.getServiceType());
            flight.setBookingDesignator(eq.getBookingDesignator());
        }
    }

    /* ================= DEI ================= */

    @Override
    protected void handleDei(String line,
                             ScheduleFlightDTO flight,
                             ScheduleLegDTO currentLeg,
                             int seq) {

        if (flight == null) {
            log.warn("DEI before FLIGHT: {}", line);
            return;
        }

        ScheduleDataElementDTO dei;

        try {
            dei = DeiParser.parse(line);
        } catch (Exception e) {
            log.warn("Invalid DEI: {}", line);
            return;
        }

        String board = normalizeStr(dei.getBoardPoint());
        String off   = normalizeStr(dei.getOffPoint());

        if (dei.isSegmentLevel()) {

            if (flight.getLegs() == null || flight.getLegs().isEmpty()) {
                addFlightLevelDei(flight, dei, seq);
                return;
            }

            if (board == null || off == null) {
                if (currentLeg != null) {
                    attachToLeg(currentLeg, dei, seq);
                    return;
                }
                addFlightLevelDei(flight, dei, seq);
                return;
            }

            ScheduleLegDTO leg = resolveLeg(flight, board, off);

            if (leg == null) {
                log.warn("Unresolved segment DEI: {}", line);
                addFlightLevelDei(flight, dei, seq);
                return;
            }

            dei.setBoardPoint(board);
            dei.setOffPoint(off);

            attachToLeg(leg, dei, seq);
            return;
        }

        if (dei.isLegLevel()) {

            ScheduleLegDTO leg = resolveLeg(flight, board, off);

            if (leg == null) {
                addFlightLevelDei(flight, dei, seq);
                return;
            }

            attachToLeg(leg, dei, seq);
            return;
        }

        addFlightLevelDei(flight, dei, seq);
    }

    /* ================= HELPERS ================= */

    private void applyRoutingChangeIfNeeded() {
        if (message.getActionType() == ActionType.ROUTING_CHANGE &&
                flight.getLegs() != null && !flight.getLegs().isEmpty()) {
            flight.getLegs().clear();
        }
    }

    private void resetContext() {
        message = null;
        flight = null;
        currentLeg = null;
    }

    private void attachToLeg(ScheduleLegDTO leg,
                             ScheduleDataElementDTO dei,
                             int seq) {

        dei.setLegSequenceNumber(leg.getLegSequenceNumber());
        dei.setSequenceOrder(seq);

        ensureLegDeiList(leg);
        leg.getDeis().add(dei);
    }

    private void ensureLegDeiList(ScheduleLegDTO leg) {
        if (leg.getDeis() == null) {
            leg.setDeis(new ArrayList<>());
        }
    }

    private String normalizeStr(String value) {
        return value != null ? value.trim().toUpperCase() : null;
    }

    private void handleSupplementary(String line) {

        if (message == null) return;

        if (flight == null) {
            log.warn("SI without flight: {}", line);
            return;
        }

        String normalized = line.trim();

        if (!normalized.toUpperCase().startsWith("SI")) {
            return;
        }

        // remove "SI "
        String value = normalized.length() > 3 ? normalized.substring(3).trim() : "";

        if (value.isBlank()) {
            log.warn("Empty SI line: {}", line);
            return;
        }

        if (flight.getSupplementaryInfo() == null) {
            flight.setSupplementaryInfo(new ArrayList<>());
        }

        // optional: avoid duplicates
        if (!flight.getSupplementaryInfo().contains(value)) {
            flight.getSupplementaryInfo().add(value);
        }
    }

    private String extractFirstToken(String line) {
        int idx = line.indexOf(' ');
        return idx > 0 ? line.substring(0, idx) : line;
    }

    private void addFlightLevelDei(ScheduleFlightDTO flight,
                                   ScheduleDataElementDTO dei,
                                   int seq) {

        if (flight.getDeis() == null) {
            flight.setDeis(new ArrayList<>());
        }

        dei.setSequenceOrder(seq);
        flight.getDeis().add(dei);
    }

    private ScheduleLegDTO resolveLeg(ScheduleFlightDTO flight, String board, String off) {

        if (flight.getLegs() == null || flight.getLegs().isEmpty()) {
            return null;
        }

        for (ScheduleLegDTO leg : flight.getLegs()) {
            if (board != null && off != null &&
                    board.equalsIgnoreCase(leg.getBoardPoint()) &&
                    off.equalsIgnoreCase(leg.getOffPoint())) {
                return leg;
            }
        }

        return null;
    }

    /* ================= BUILD ================= */

    @Override
    protected ScheduleMessageDTO buildContext(ScheduleGroupedMessage grouped) {

        String raw = String.join("\n", grouped.getOriginalLines());

        List<ScheduleSubMessageDTO> messages = envelope.getMessages();

        for (ScheduleSubMessageDTO msg : messages) {
            msg.setRawMessage(raw); // ✅ FIXED
        }

        return envelope;
    }
}
