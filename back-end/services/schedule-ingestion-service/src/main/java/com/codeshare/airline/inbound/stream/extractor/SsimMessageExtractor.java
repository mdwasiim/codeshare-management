package com.codeshare.airline.inbound.stream.extractor;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.inbound.common.reader.LineReader;
import com.codeshare.airline.inbound.orchestration.handler.StreamExtractorHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public final class SsimMessageExtractor implements StreamExtractorHandler {

    private static final int DEFAULT_FLIGHT_GROUPS_PER_BATCH = 5_000;

    private final int maxFlightGroupsPerBatch;

    public SsimMessageExtractor(MessageType messageType) {
        this(messageType, DEFAULT_FLIGHT_GROUPS_PER_BATCH);
    }

    public SsimMessageExtractor(MessageType messageType, int maxFlightGroupsPerBatch) {
        this.maxFlightGroupsPerBatch = maxFlightGroupsPerBatch > 0
                ? maxFlightGroupsPerBatch
                : DEFAULT_FLIGHT_GROUPS_PER_BATCH;
    }

    @Override
    public MessageType supportedType() {
        return MessageType.SSIM;
    }

    @Override
    public boolean isParallelSafe() {
        return false;
    }

    @Override
    public boolean isBatchSupported() {
        return true;
    }

    @Override
    public void extract(InputStream inputStream, Consumer<List<String>> consumer) {

        LineReader reader = new LineReader(inputStream);
        List<String> header = new ArrayList<>();
        List<String> currentCarrier = new ArrayList<>();
        List<String> currentTrailer = new ArrayList<>();
        List<List<String>> currentFlightGroups = new ArrayList<>();
        List<String> currentFlightGroup = new ArrayList<>();
        String currentFlightKey = null;

        try {
            String line;

            while ((line = reader.nextLine()) != null) {
                String normalized = line.trim();

                if (normalized.isEmpty()) {
                    continue;
                }

                // SSIM files can contain 200-byte zero padding records between real records.
                if (normalized.charAt(0) == '0') {
                    continue;
                }

                char recordType = normalized.charAt(0);
                switch (recordType) {
                    case '1' -> header.add(line);
                    case '2' -> {
                        flushSection(header, currentCarrier, currentFlightGroups, currentFlightGroup, currentTrailer, consumer);
                        currentCarrier = new ArrayList<>();
                        currentCarrier.add(line);
                        currentFlightGroups = new ArrayList<>();
                        currentFlightGroup = new ArrayList<>();
                        currentTrailer = new ArrayList<>();
                        currentFlightKey = null;
                    }
                    case '3' -> {
                        String flightKey = flightKey(line);
                        if (!currentFlightGroup.isEmpty() && !flightKey.equals(currentFlightKey)) {
                            currentFlightGroups.add(currentFlightGroup);
                            if (currentFlightGroups.size() > maxFlightGroupsPerBatch) {
                                flushCompleteGroups(header, currentCarrier, currentFlightGroups, consumer);
                                currentFlightGroups = new ArrayList<>();
                            }
                            currentFlightGroup = new ArrayList<>();
                        }
                        currentFlightKey = flightKey;
                        currentFlightGroup.add(line);
                    }
                    case '4' -> currentFlightGroup.add(line);
                    case '5' -> currentTrailer.add(line);
                    default -> currentFlightGroup.add(line);
                }
            }

            flushSection(header, currentCarrier, currentFlightGroups, currentFlightGroup, currentTrailer, consumer);

        } catch (Exception ex) {
            log.error("SSIM extraction failed", ex);
            throw new IllegalStateException("SSIM extraction failed", ex);
        }
    }

    private void flushSection(
            List<String> header,
            List<String> carrier,
            List<List<String>> flightGroups,
            List<String> currentFlightGroup,
            List<String> trailer,
            Consumer<List<String>> consumer
    ) {

        if (!currentFlightGroup.isEmpty()) {
            flightGroups.add(currentFlightGroup);
        }

        if (carrier.isEmpty() && flightGroups.isEmpty() && trailer.isEmpty()) {
            return;
        }

        if (flightGroups.isEmpty()) {
            List<String> metadataOnly = new ArrayList<>(header.size() + carrier.size() + trailer.size());
            metadataOnly.addAll(header);
            metadataOnly.addAll(carrier);
            metadataOnly.addAll(trailer);
            consumer.accept(metadataOnly);
            return;
        }

        int flightLineCount = flightGroups.stream().mapToInt(List::size).sum();
        List<String> batch = new ArrayList<>(header.size() + carrier.size() + flightLineCount + trailer.size());
        batch.addAll(header);
        batch.addAll(carrier);
        for (List<String> flightGroup : flightGroups) {
            batch.addAll(flightGroup);
        }
        batch.addAll(trailer);
        consumer.accept(batch);
    }

    private void flushCompleteGroups(
            List<String> header,
            List<String> carrier,
            List<List<String>> flightGroups,
            Consumer<List<String>> consumer
    ) {
        if (flightGroups.isEmpty()) {
            return;
        }

        int flightLineCount = flightGroups.stream().mapToInt(List::size).sum();
        List<String> batch = new ArrayList<>(header.size() + carrier.size() + flightLineCount);
        batch.addAll(header);
        batch.addAll(carrier);
        for (List<String> flightGroup : flightGroups) {
            batch.addAll(flightGroup);
        }
        consumer.accept(batch);
    }

    private String flightKey(String line) {
        if (line.length() < 11) {
            return line;
        }
        return line.substring(1, 11).trim();
    }
}
