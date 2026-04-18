package com.codeshare.airline.inbound.common.classifier;

import com.codeshare.airline.inbound.domain.context.GenericLineClassifierContext;

public interface LineClassifier {

    void reset();

    GenericLineClassifierContext classify(String line);

}