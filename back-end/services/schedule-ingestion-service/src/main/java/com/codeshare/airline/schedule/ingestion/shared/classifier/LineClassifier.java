package com.codeshare.airline.schedule.ingestion.shared.classifier;

import com.codeshare.airline.schedule.ingestion.domain.context.GenericLineClassifierContext;

public interface LineClassifier {

    void reset();

    GenericLineClassifierContext classify(String line);

}