package com.codeshare.airline.schedule.parsing.asm.parser;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmFlightBlock;
import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AsmParser {

    private final AsmContinuationResolver continuationResolver;
    private final AsmFlightBlockBuilder blockBuilder;
    private final AsmRoutingParser routingParser;
    private final AsmDeiParser deiParser;

    public List<AsmInboundMessage> parse(String rawContent) {

        List<String> rawLines = Arrays.asList(rawContent.split("\n"));

        List<String> resolved =
                continuationResolver.resolve(rawLines);

        List<AsmFlightBlock> blocks =
                blockBuilder.build(resolved);

        List<AsmInboundMessage> messages = new ArrayList<>();

        for (AsmFlightBlock block : blocks) {
            messages.add(parseBlock(block));
        }

        return messages;
    }

    private AsmInboundMessage parseBlock(AsmFlightBlock block) {

        AsmInboundMessage msg = new AsmInboundMessage();

        msg.setActionIdentifier(block.getActionIdentifier());

        if (block.getFlightLine() != null) {
            parseFlight(block.getFlightLine(), msg);
        }

        if (block.getPeriodLine() != null) {
            parsePeriod(block.getPeriodLine(), msg);
        }

        if (block.getDaysLine() != null) {
            msg.setDaysOfOperation(block.getDaysLine());
        }

        if (block.getRoutingLine() != null) {
            msg.setLegs(
                    routingParser.parse(block.getRoutingLine())
            );
        }

        msg.setDeis(
                deiParser.parse(block.getDeiLines())
        );

        if (!block.getEquipmentLines().isEmpty()) {
            msg.setAircraftType(
                    extractAircraftType(block.getEquipmentLines().get(0))
            );
        }

        return msg;
    }

    private void parseFlight(String line, AsmInboundMessage msg) {

        String trimmed = line.trim();

        // Expected: QR123A or QR123
        if (!trimmed.matches("^[A-Z0-9]{2}\\d+[A-Z]?$")) {
            throw new IllegalArgumentException(
                    "Invalid ASM flight line format: " + line
            );
        }

        String carrier = trimmed.substring(0, 2);
        String remaining = trimmed.substring(2);

        String flightNumber;
        String suffix = null;

        // If last character is letter → suffix exists
        if (Character.isLetter(remaining.charAt(remaining.length() - 1))) {
            suffix = remaining.substring(remaining.length() - 1);
            flightNumber = remaining.substring(0, remaining.length() - 1);
        } else {
            flightNumber = remaining;
        }

        msg.setCarrier(carrier);
        msg.setFlightNumber(flightNumber);
        msg.setSuffix(suffix);
    }

    private void parsePeriod(String line, AsmInboundMessage msg) {

        String trimmed = line.trim();

        if (!trimmed.matches("\\d{2}[A-Z]{3}-\\d{2}[A-Z]{3}")) {
            throw new IllegalArgumentException(
                    "Invalid ASM period format: " + line
            );
        }

        String[] parts = trimmed.split("-");

        LocalDate from = parseIataDate(parts[0]);
        LocalDate to = parseIataDate(parts[1]);

        // Handle season crossing (e.g. DEC → MAR next year)
        if (to.isBefore(from)) {
            to = to.plusYears(1);
        }

        msg.setPeriodFrom(from);
        msg.setPeriodTo(to);
    }

    private LocalDate parseIataDate(String token) {

        int day = Integer.parseInt(token.substring(0, 2));
        String monthStr = token.substring(2, 5);

        Month month = parseMonth(monthStr);

        int currentYear = LocalDate.now().getYear();

        return LocalDate.of(currentYear, month, day);
    }

    private Month parseMonth(String monthStr) {

        return switch (monthStr) {
            case "JAN" -> Month.JANUARY;
            case "FEB" -> Month.FEBRUARY;
            case "MAR" -> Month.MARCH;
            case "APR" -> Month.APRIL;
            case "MAY" -> Month.MAY;
            case "JUN" -> Month.JUNE;
            case "JUL" -> Month.JULY;
            case "AUG" -> Month.AUGUST;
            case "SEP" -> Month.SEPTEMBER;
            case "OCT" -> Month.OCTOBER;
            case "NOV" -> Month.NOVEMBER;
            case "DEC" -> Month.DECEMBER;
            default -> throw new IllegalArgumentException(
                    "Invalid IATA month: " + monthStr);
        };
    }

    private String extractAircraftType(String eqtLine) {
        String[] parts = eqtLine.split("\\s+");
        return parts.length > 1 ? parts[1] : null;
    }
}