package com.codeshare.airline.schedule.ingestion.shared.classifier;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;

public class LineClassifierFactory {

    private LineClassifierFactory() {
    }

    public static LineClassifier forMessage(MessageType type) {

        return switch (type) {
            case SSIM -> throw new IllegalArgumentException("SSIM does not use line classification");
            case SSM -> new GenericLineClassifier(new SsmLineClassifierStrategy());
            case ASM -> new GenericLineClassifier(new AsmLineClassifierStrategy());
        };
    }
}
