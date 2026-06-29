package com.codeshare.airline.inbound.orchestration.parsers;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.common.classifier.LineClassifier;
import com.codeshare.airline.inbound.common.classifier.LineClassifierFactory;
import com.codeshare.airline.inbound.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.domain.enums.TimeMode;
import com.codeshare.airline.inbound.dto.schedule.*;
import com.codeshare.airline.inbound.mappers.schedule.ActionTypeMapper;
import com.codeshare.airline.inbound.orchestration.parsers.shared.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SsmMessageParser extends AbstractScheduleParser<ScheduleMessageDTO> {

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
        return LineClassifierFactory.create(MessageType.SSM);
    }

    /* ================= MAIN ================= */

    @Override
    protected void handleLine(GenericLineClassifierContext ctx) {

        String line = ctx.getNormalizedLine();

        // raw line tracking (added for consistency)
        if (message != null) {
            if (message.getRawLines() == null) {
                message.setRawLines(new ArrayList<>());
            }
            message.getRawLines().add(line);
        }

        switch (ctx.getType()) {

            /* ================= HEADER ================= */

            case HEADER -> {
                try {
                    envelope.setMessageType(MessageType.valueOf(line));
                } catch (Exception e) {
                    log.warn("Invalid message type: {}", line);
                }
            }

            case TIME_MODE -> pendingTimeMode = TimeMode.valueOf(line);

            case MESSAGE_REFERENCE -> envelope.setRawHeader(line);

            /* ================= ACTION ================= */

            case ACTION -> handleAction(line);

            /* ================= FLIGHT ================= */

            case FLIGHT -> handleFlight(line);

            /* ================= PERIOD ================= */

            case PERIOD -> handlePeriod(line);

            /* ================= LEG ================= */

            case LEG -> handleLeg(line);

            /* ================= TIME ================= */

            case TIME -> handleTime(line); // ✅ ADDED

            /* ================= EQUIPMENT ================= */

            case EQUIPMENT_AND_SERVICE -> handleEquipment(line);

            /* ================= DEI ================= */

            case DEI -> handleDei(line, flight, currentLeg, globalDeiSeq++);

            /* ================= SUPPLEMENTARY ================= */

            case SUPPLEMENTARY -> handleSupplementary(line);

            /* ================= SEPARATOR ================= */

            case SUB_MESSAGE_SEPARATOR -> resetContext();

            default -> log.debug("Ignoring line: {}", line);
        }
    }

    /* ================= ACTION ================= */

    private void handleAction(String line) {

        message = new ScheduleSubMessageDTO();
        envelope.addMessage(message);

        message.setActionType(ActionTypeMapper.fromSsm(extractFirstToken(line)));

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

        var identity = FlightIdentityParser.parseSsm(line);

        flight.setAirlineDesignator(identity.getAirlineDesignator());
        flight.setFlightNumber(identity.getFlightNumber());
        flight.setOperationalSuffix(identity.getOperationalSuffix());

        currentLeg = null;
    }

    /* ================= PERIOD ================= */

    private void handlePeriod(String line) {

        if (flight == null) {
            throw new IllegalStateException("PERIOD before FLIGHT: " + line);
        }

        flight.addPeriod(PeriodParser.parse(line));
    }

    /* ================= LEG ================= */

    private void handleLeg(String line) {

        if (flight == null) {
            throw new IllegalStateException("LEG before FLIGHT: " + line);
        }

        ScheduleLegDTO leg =
                LegParser.parse(line, flight.getLegs().size() + 1);

        flight.addLeg(leg);
        currentLeg = leg;
    }

    /* ================= TIME ================= */
   private void handleTime(String line) {

       if (message == null || currentLeg == null) {
           throw new IllegalStateException("TIME without context: " + line);
       }

       var times = TimeParser.parse(line);

       // 🔥 DO NOT overwrite existing time
       if (currentLeg.getDepartureTime() == null) {
           currentLeg.setDepartureTime(times.getStd());
           currentLeg.setArrivalTime(times.getSta());
           currentLeg.setDepartureDayOffset(times.getDepOffset());
           currentLeg.setArrivalDayOffset(times.getArrOffset());
       } else {
           log.warn("TIME ignored (already set from LEG): {}", line);
       }
   }

    /* ================= EQUIPMENT ================= */

    private void handleEquipment(String line) {

        if (flight == null) {
            throw new IllegalStateException("EQUIPMENT before FLIGHT: " + line);
        }

        var eq = EquipmentParser.parse(line);

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

        ScheduleDataElementDTO dei = DeiParser.parse(line);

        String board = normalizeStr(dei.getBoardPoint());
        String off   = normalizeStr(dei.getOffPoint());

        if (dei.isSegmentLevel()) {

            if (flight.getLegs() == null || flight.getLegs().isEmpty()) {
                throw new IllegalStateException("SSM: Segment DEI without legs: " + line);
            }

            if (board == null || off == null) {

                if (currentLeg != null) {
                    attachToLeg(currentLeg, dei, seq);
                    return;
                }

                throw new IllegalStateException("SSM: Segment DEI missing board/off: " + line);
            }

            ScheduleLegDTO leg = resolveLeg(flight, board, off);

            if (leg == null) {
                throw new IllegalStateException("SSM: Invalid segment DEI: " + line);
            }

            dei.setBoardPoint(board);
            dei.setOffPoint(off);

            attachToLeg(leg, dei, seq);
            return;
        }

        if (dei.isLegLevel()) {

            ScheduleLegDTO leg = resolveLeg(flight, board, off);

            if (leg == null) {
                throw new IllegalStateException("SSM: Unable to resolve LEG DEI: " + line);
            }

            attachToLeg(leg, dei, seq);
            return;
        }

        addFlightLevelDei(flight, dei, seq);
    }

    /* ================= HELPERS ================= */

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

        if (flight == null) {
            throw new IllegalStateException("SI before FLIGHT: " + line);
        }

        if (flight.getSupplementaryInfo() == null) {
            flight.setSupplementaryInfo(new ArrayList<>());
        }

        flight.getSupplementaryInfo().add(line);
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

        if (board != null && off != null) {
            for (ScheduleLegDTO leg : flight.getLegs()) {
                if (board.equalsIgnoreCase(leg.getBoardPoint()) &&
                        off.equalsIgnoreCase(leg.getOffPoint())) {
                    return leg;
                }
            }
        }

        if (board != null) {
            for (ScheduleLegDTO leg : flight.getLegs()) {
                if (board.equalsIgnoreCase(leg.getBoardPoint())) {
                    return leg;
                }
            }
        }

        return currentLeg;
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
