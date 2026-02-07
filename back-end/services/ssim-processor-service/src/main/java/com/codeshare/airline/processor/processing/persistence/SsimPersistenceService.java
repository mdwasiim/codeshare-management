package com.codeshare.airline.processor.processing.persistence;

import com.codeshare.airline.processor.pipeline.enm.SsimLoadStatus;
import com.codeshare.airline.processor.pipeline.model.ParsedSsimResult;
import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;

public interface SsimPersistenceService {

    SsimLoadContext persist(ParsedSsimResult parsed);

    SsimLoadContext updateStatus(SsimLoadContext loadContext, SsimLoadStatus ssimLoadStatus);
}
