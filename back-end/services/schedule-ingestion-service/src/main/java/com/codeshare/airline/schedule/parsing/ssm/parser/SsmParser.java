package com.codeshare.airline.schedule.parsing.ssm.parser;


import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmFlightBlock;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundLeg;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SsmParser {

    private final SsmContinuationResolver continuationResolver;
    private final SsmFlightBlockBuilder blockBuilder;
    private final SsmRoutingParser routingParser;
    private final SsmDeiParser deiParser;

    public List<SsmInboundMessage> parse(String rawContent) {

        List<String> rawLines = Arrays.asList(rawContent.split("\n"));

        List<String> resolvedLines =
                continuationResolver.resolve(rawLines);

        List<SsmFlightBlock> blocks =
                blockBuilder.build(resolvedLines);

        List<SsmInboundMessage> messages = new ArrayList<>();

        for (SsmFlightBlock block : blocks) {
            messages.add(parseBlock(block));
        }

        return messages;
    }

    private SsmInboundMessage parseBlock(SsmFlightBlock block) {

        SsmInboundMessage message = new SsmInboundMessage();

        message.setActionIdentifier(
                ActionIdentifier.from(block.getActionIdentifier())
        );

        parseFlight(block.getFlightDesignatorLine(), message);

        List<SsmInboundLeg> ssmInboundLegs = routingParser.parse(block.getRoutingLine());
        message.setLegs(ssmInboundLegs);

        message.setDeis(
                deiParser.parse(block.getDeiLines())
        );

        return message;
    }
    private void parseFlight(String line, SsmInboundMessage message) {

        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Missing flight designator line");
        }

        String flightPart;
        String datePart;

        // Handle both QR123/15DEC and QR123 15DEC
        if (line.contains("/")) {
            String[] parts = line.split("/");
            flightPart = parts[0].trim();
            datePart = parts[1].trim();
        } else {
            String[] parts = line.split("\\s+");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid flight designator format: " + line);
            }
            flightPart = parts[0].trim();
            datePart = parts[1].trim();
        }

        // Extract carrier (first 2 letters)
        if (flightPart.length() < 3) {
            throw new IllegalArgumentException("Invalid flight number format: " + flightPart);
        }

        String carrier = flightPart.substring(0, 2);
        String flightNumberWithSuffix = flightPart.substring(2);

        message.setCarrier(carrier);

        // Handle suffix (e.g., 123A)
        String numericPart = flightNumberWithSuffix.replaceAll("[^0-9]", "");
        String suffixPart = flightNumberWithSuffix.replaceAll("[0-9]", "");

        message.setFlightNumber(numericPart);

        if (!suffixPart.isEmpty()) {
            message.setSuffix(suffixPart);   // <-- add suffix field in DTO
        }

        // Parse IATA date (ddMMM)
        message.setOperationDate(parseIataDate(datePart));
    }

    private LocalDate parseIataDate(String token) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("ddMMMyyyy", Locale.ENGLISH);

        int currentYear = Year.now().getValue();

        LocalDate parsed = LocalDate.parse(token + currentYear, formatter);

        // If parsed date is more than 6 months in future, adjust backward year
        if (parsed.isAfter(LocalDate.now().plusMonths(6))) {
            parsed = parsed.minusYears(1);
        }

        return parsed;
    }
}