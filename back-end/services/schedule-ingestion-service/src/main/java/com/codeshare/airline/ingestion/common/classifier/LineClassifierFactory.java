package com.codeshare.airline.ingestion.common.classifier;

import com.codeshare.airline.core.enums.MessageType;

public class LineClassifierFactory {

    public static LineClassifier create(MessageType type) {

        return switch (type) {
            case SSIM -> null;
            case SSM -> new GenericLineClassifier(new SsmLineClassifierStrategy());
            case ASM -> new GenericLineClassifier(new AsmLineClassifierStrategy());
        };
    }
}