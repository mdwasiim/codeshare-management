package com.codeshare.airline.ingestion;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.common.classifier.LineClassifier;
import com.codeshare.airline.ingestion.common.classifier.LineClassifierFactory;
import com.codeshare.airline.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.ingestion.domain.enums.ActionType;
import com.codeshare.airline.ingestion.domain.enums.ScheduleLineIdentifier;
import com.codeshare.airline.ingestion.orchestration.parsers.AsmMessageParser;
import com.codeshare.airline.ingestion.orchestration.parsers.SsmMessageParser;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.ingestion.stream.extractor.GenericMessageExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MessageFileProcessor {

    public static void main(String[] args) throws IOException {

        // 🔥 SWITCH HERE
        MessageType type = MessageType.ASM;

        String filePath = switch (type) {
            case SSM -> "C:\\Users\\mdwas\\Projects\\codeBase\\codeshare-management\\back-end\\services\\schedule-ingestion-service\\src\\main\\resources\\100_SSM_QR_Test_Messages.txt";
            case ASM -> "C:\\Users\\mdwas\\Projects\\codeBase\\codeshare-management\\back-end\\services\\schedule-ingestion-service\\src\\main\\resources\\100_ASM_QR_Test_Messages.txt";
            default -> throw new IllegalStateException("Unsupported type");
        };

        GenericMessageExtractor extractor = new GenericMessageExtractor(type);


        try (InputStream is = Files.newInputStream(Path.of(filePath))) {

            System.out.println("🚀 Processing " + type + " File...\n");

            extractor.extract(is, messageLines -> {

                System.out.println("\n==============================");
                System.out.println("📩 NEW " + type + " MESSAGE");
                System.out.println("==============================");

                processMessage(messageLines, type);
            });
        }

        System.out.println("\n🎉 FILE PROCESSING COMPLETED");
    }

    private static void processMessage(List<String> lines, MessageType type) {

        // 🔥 PRINT ORIGINAL MESSAGE
        System.out.println("\n🧾 ORIGINAL MESSAGE:");
        System.out.println("----------------------------------");

        for (String line : lines) {
            System.out.println(line);
        }

        System.out.println("----------------------------------\n");

        // ================= CLASSIFIER =================
        LineClassifier classifier = LineClassifierFactory.create(type);

        assert classifier != null;
        classifier.reset();

        ActionType action = null;

        for (String line : lines) {

            String normalized = line.trim();

            GenericLineClassifierContext ctx = classifier.classify(normalized);

            ScheduleLineIdentifier id = ctx.getType();

            if (ctx.getActionType() != null) {
                action = ctx.getActionType();
            }

            if (id == ScheduleLineIdentifier.UNKNOWN) {
                System.err.println("⚠ UNKNOWN: " + normalized);
            }

            System.out.printf("%-40s → %s%n", normalized, id);
        }
        // ================= PARSER =================
        try {

            ScheduleMessageDTO result = parse(lines, type);

            validate(result);

            System.out.println("✅ PARSE SUCCESS");

        } catch (Exception e) {

            System.err.println("❌ PARSE FAILED: " + e.getMessage());
        }
    }

    /* ================= PARSER ================= */

    private static ScheduleMessageDTO parse(List<String> lines, MessageType type) {

        return switch (type) {

            case ASM -> {
                AsmMessageParser parser = new AsmMessageParser();
                ScheduleGroupedMessage grouped = parser.groupMessage(lines);
                yield parser.parseMessage(grouped);
            }

            case SSM -> {
                SsmMessageParser parser = new SsmMessageParser();
                ScheduleGroupedMessage grouped = parser.groupMessage(lines);
                yield parser.parseMessage(grouped);
            }

            default -> throw new IllegalStateException("Unsupported type");
        };
    }

    /* ================= VALIDATION ================= */

    private static void validate(ScheduleMessageDTO dto) {

        if (dto.getMessages() == null || dto.getMessages().isEmpty()) {
            throw new RuntimeException("No sub-messages found");
        }

        dto.getMessages().forEach(msg -> {

            if (msg.getFlights() == null || msg.getFlights().isEmpty()) {
                throw new RuntimeException("No flights in message");
            }

            msg.getFlights().forEach(flight -> {

                if (flight.getLegs() != null) {

                    flight.getLegs().forEach(leg -> {

                        if (leg.getBoardPoint() == null || leg.getOffPoint() == null) {
                            throw new RuntimeException("Invalid leg routing");
                        }

                        if (leg.getDepartureTime() == null) {
                            System.out.println("⚠ Missing departure time");
                        }
                    });
                }
            });
        });
    }
}