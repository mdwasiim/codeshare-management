package com.codeshare.airline.ingestion.stream.extractor;

import com.codeshare.airline.ingestion.common.reader.LineReader;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.orchestration.handler.StreamExtractorHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public final class SsimMessageExtractor implements StreamExtractorHandler {

    public SsimMessageExtractor(MessageType messageType) {
    }

    @Override
    public MessageType supportedType() {
        return MessageType.SSIM;
    }

    @Override
    public boolean isParallelSafe() {
        return true;
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    @Override
    public void extract(InputStream inputStream, Consumer<List<String>> consumer) {

        LineReader reader = new LineReader(inputStream);

        List<String> currentFlightData = new ArrayList<>();
        String currentFlightKey = null;

        int lineNumber = 0;

        try {
            String line;

            while ((line = reader.nextLine()) != null) {

                lineNumber++;

                String normalized = line.trim();
                if (normalized.isEmpty()) continue;

                char type = normalized.charAt(0);

                //  Skip padding records
                if (type == '0') {
                    continue;
                }

                switch (type) {

                    /* ================= FILE LEVEL ================= */

                    case '1', '2' -> {
                        flushFlight(currentFlightData, currentFlightKey, consumer);
                        currentFlightKey = null;
                        consumer.accept(single(line));
                    }

                    /* ================= FLIGHT START ================= */

                    case '3' -> {

                        String newFlightKey = extractFlightKey(normalized);

                        // new flight detected
                        if (currentFlightKey != null && !currentFlightKey.equals(newFlightKey)) {
                            flushFlight(currentFlightData, currentFlightKey, consumer);
                        }

                        currentFlightKey = newFlightKey;
                        currentFlightData.add(line);
                    }

                    /* ================= FLIGHT CONTINUATION ================= */

                    case '4' -> {

                        if (currentFlightData.isEmpty()) {
                            log.warn("⚠️ Orphan TYPE 4 at line {}: {}", lineNumber, line);
                            continue; // ❗ skip instead of corrupting
                        }

                        currentFlightData.add(line);
                    }

                    /* ================= TRAILER ================= */

                    case '5' -> {
                        flushFlight(currentFlightData, currentFlightKey, consumer);
                        currentFlightKey = null;
                        consumer.accept(single(line));
                    }

                    /* ================= UNKNOWN ================= */

                    default -> {
                        log.warn("⚠️ Unknown record type at line {}: {}", lineNumber, line);
                    }
                }
            }

            // final flush
            flushFlight(currentFlightData, currentFlightKey, consumer);

        } catch (Exception ex) {
            log.error(" SSIM extraction failed", ex);
            throw new IllegalStateException("SSIM extraction failed", ex);
        }
    }

    /* ================= FLUSH ================= */

    private static void flushFlight(List<String> data,
                                    String flightKey,
                                    Consumer<List<String>> consumer) {

        if (!data.isEmpty()) {
            log.debug("✈️ Processing flightKey={} records={}", flightKey, data.size());
            consumer.accept(new ArrayList<>(data));
            data.clear();
        }
    }

    /* ================= SINGLE ================= */

    private static List<String> single(String line) {
        return List.of(line);
    }

    /* ================= FLIGHT KEY ================= */

    private static String extractFlightKey(String line) {

        try {
            // remove record type
            String content = line.substring(1);

            // SSIM fixed format (safe substring)
            String airline = safeSubstring(content, 0, 3).trim();
            String flightNumber = safeSubstring(content, 3, 8).trim();
            String suffix = safeSubstring(content, 8, 10).trim();

            return airline + flightNumber + suffix;

        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    private static String safeSubstring(String s, int start, int end) {
        if (s.length() >= end) return s.substring(start, end);
        if (s.length() > start) return s.substring(start);
        return "";
    }
}