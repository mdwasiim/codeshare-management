package com.codeshare.airline.inbound.orchestration.parsers;

import com.codeshare.airline.inbound.common.classifier.LineClassifier;
import com.codeshare.airline.inbound.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.domain.enums.ScheduleLineIdentifier;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFlightDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleLegDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractScheduleParser<C> implements ScheduleParser<C> {

    @Override
    public ScheduleGroupedMessage groupMessage(List<String> lines) {

        Map<ScheduleLineIdentifier, List<String>> grouped = new EnumMap<>(ScheduleLineIdentifier.class);
        List<String> messageLines = new ArrayList<>();
        List<GenericLineClassifierContext> ordered = new ArrayList<>();

        LineClassifier classifier = classifier();
        classifier.reset();

        int lineNumber = 0;

        for (String rawLine : lines) {

            lineNumber++;

            if (rawLine == null || rawLine.isBlank()) continue;

            String line = normalize(rawLine);

            messageLines.add(line);

            GenericLineClassifierContext lc = classifier.classify(line);

            if (lc.getType() == ScheduleLineIdentifier.UNKNOWN) {
                log.warn("Unknown line {}: {}", lineNumber, rawLine);
                continue;
            }

            // group
            grouped.computeIfAbsent(lc.getType(), k -> new ArrayList<>()) .add(lc.getNormalizedLine());

            // preserve order
            ordered.add(lc);

        }

        return new ScheduleGroupedMessage(grouped, ordered, messageLines);
    }


    @Override
    public C parseMessage(ScheduleGroupedMessage grouped) {
        init();
        for (GenericLineClassifierContext line : grouped.getOrderedLines()) {
            handleLine(line);
        }

        return buildContext(grouped);
    }

    protected String normalize(String line) {
        return line.trim().replaceAll("\\s+", " ");
    }

    protected abstract LineClassifier classifier();

    protected abstract void init();

    protected abstract void handleDei(String line, ScheduleFlightDTO flight, ScheduleLegDTO currentLeg,int seq);

    protected abstract void handleLine(GenericLineClassifierContext lineClassifierContext);

    protected abstract C buildContext(ScheduleGroupedMessage grouped);
}