package com.codeshare.airline.ingestion.common.classifier;

import com.codeshare.airline.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.ingestion.domain.enums.ScheduleLineIdentifier;

interface LineClassifierStrategy {
    GenericLineClassifierContext classify(String line, ScheduleLineIdentifier lastType);
}