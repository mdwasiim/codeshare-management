package com.codeshare.airline.inbound.common.classifier;

import com.codeshare.airline.inbound.domain.context.GenericLineClassifierContext;
import com.codeshare.airline.inbound.domain.enums.ScheduleLineIdentifier;

interface LineClassifierStrategy {
    GenericLineClassifierContext classify(String line, ScheduleLineIdentifier lastType);
}