package com.codeshare.airline.inbound.common.classifier;

import com.codeshare.airline.inbound.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.inbound.domain.enums.ScheduleLineIdentifier;
import lombok.RequiredArgsConstructor;

import static com.codeshare.airline.inbound.domain.enums.ScheduleLineIdentifier.UNKNOWN;

@RequiredArgsConstructor
public class GenericLineClassifier implements LineClassifier {

    private final LineClassifierStrategy strategy;
    private ScheduleLineIdentifier lastType;

    @Override
    public void reset() {
        lastType = null;
    }

    @Override
    public GenericLineClassifierContext classify(String line) {

        if (line == null || line.isBlank()) {
            lastType = UNKNOWN;
            return new GenericLineClassifierContext(UNKNOWN, line, line);
        }

        GenericLineClassifierContext ctx = strategy.classify(line, lastType);
        lastType = ctx.getType();

        return ctx;
    }
}