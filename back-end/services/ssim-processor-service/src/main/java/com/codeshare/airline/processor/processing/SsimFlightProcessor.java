package com.codeshare.airline.processor.processing;

import com.codeshare.airline.processor.domain.event.SsimFlightBlockReceivedEvent;
import com.codeshare.airline.processor.domain.model.ParsedFlightSchedule;
import com.codeshare.airline.processor.processing.persistence.SsimPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsimFlightProcessor {

    private final SsimParser parser;
    private final SsimBusinessValidator validator;
    private final SsimPersistenceService persistenceService;

    public void process(SsimFlightBlockReceivedEvent event) {

        ParsedFlightSchedule schedule =
                parser.parse(event.getRawSsimBlock());

        validator.validate(schedule);

        persistenceService.save(schedule);
    }
}
