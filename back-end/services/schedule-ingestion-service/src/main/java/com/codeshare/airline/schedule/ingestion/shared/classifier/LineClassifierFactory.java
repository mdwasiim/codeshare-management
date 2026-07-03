package com.codeshare.airline.schedule.ingestion.shared.classifier;

import com.codeshare.airline.core.enums.schedule.MessageType;

public class LineClassifierFactory {

    public static LineClassifier create(MessageType type) {

        return switch (type) {
            case SSIM -> null;
            case SSM -> new GenericLineClassifier(new SsmLineClassifierStrategy());
            case ASM -> new GenericLineClassifier(new AsmLineClassifierStrategy());
        };
    }
}