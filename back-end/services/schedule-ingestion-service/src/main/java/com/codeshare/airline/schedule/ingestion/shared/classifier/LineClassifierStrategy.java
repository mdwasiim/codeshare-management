package com.codeshare.airline.schedule.ingestion.shared.classifier;

import com.codeshare.airline.schedule.ingestion.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleLineIdentifier;

interface LineClassifierStrategy {
    GenericLineClassifierContext classify(String line, ScheduleLineIdentifier lastType);
}