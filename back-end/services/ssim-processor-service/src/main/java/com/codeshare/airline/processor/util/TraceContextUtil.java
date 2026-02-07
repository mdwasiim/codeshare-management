package com.codeshare.airline.processor.util;

import com.codeshare.airline.processor.domain.event.SsimFlightBlockReceivedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TraceContextUtil {

    public static void logContext(SsimFlightBlockReceivedEvent event) {
        log.debug(
                "traceId={} tenant={} flight={}",
                event.getClass(),
                event.getTenantId(),
                event.getFlightNumber()
        );
    }
}
