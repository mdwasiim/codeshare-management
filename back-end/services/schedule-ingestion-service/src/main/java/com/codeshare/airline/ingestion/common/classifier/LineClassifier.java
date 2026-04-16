package com.codeshare.airline.ingestion.common.classifier;

import com.codeshare.airline.ingestion.domain.context.GenericLineClassifierContext;

public interface LineClassifier {

    void reset();

    GenericLineClassifierContext classify(String line);

}