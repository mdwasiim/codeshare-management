package com.codeshare.airline.processor.expansion;


import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;

public interface FlightScheduleExpander {
    void expand(SsimLoadContext context);
}
