package com.codeshare.airline.kafka.tracing;

import org.slf4j.MDC;

public final class KafkaMdcUtil {

    private KafkaMdcUtil() {}

    public static void put(String traceId, String tenantId) {
        if (traceId != null) {
            MDC.put("traceId", traceId);
        }
        if (tenantId != null) {
            MDC.put("tenantId", tenantId);
        }
    }

    public static void clear() {
        MDC.remove("traceId");
        MDC.remove("tenantId");
    }
}
