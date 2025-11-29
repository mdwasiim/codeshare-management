package com.codeshare.airline.common.httpTransaction;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public final class TransactionIdGenerator {

    private static final AtomicLong COUNTER = new AtomicLong(1);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private TransactionIdGenerator() {}

    public static String nextId(String prefix) {
        long count = COUNTER.getAndIncrement();
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return prefix + "-" + timestamp + String.format("%04d", count % 10000);
    }

}
