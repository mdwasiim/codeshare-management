package com.codeshare.airline.kafka.tracing;

public final class KafkaTraceHeaders {

    private KafkaTraceHeaders() {}

    public static final String TRACE_ID = "X-Trace-Id";
    public static final String TENANT_ID = "X-Tenant-Id";
    public static final String SOURCE   = "X-Source";
}

