package com.codeshare.airline.processor.parsing.model;

import com.codeshare.airline.processor.entities.ScheduleVersion;
import com.codeshare.airline.processor.entities.ssim.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SsimLoadContext {
    private final SsimR1Header header;
    private final ScheduleVersion scheduleVersion;
    private final List<SsimR3FlightLeg> flightLegs;
    private final List<SsimR4DateVariation> dateVariations;
    private final List<SsimR5DateVariationContinuation> dateVariationContinuations;
    // 👇 ADD THIS
    private final List<SsimAppendixHCodeshare> codeshares;
}
