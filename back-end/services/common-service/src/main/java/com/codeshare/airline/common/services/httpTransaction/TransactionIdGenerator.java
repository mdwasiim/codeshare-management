package com.codeshare.airline.common.services.httpTransaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class TransactionIdGenerator {

    // yyyyMMddHHmmssSSS → 17 digits
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private TransactionIdGenerator() {}

    public static String nextId(String prefix) {

        // Time component (sortable, cluster-safe)
        String timestamp = LocalDateTime.now().format(FORMATTER);

        // Random 5-digit number (00000–99999)
        int random = ThreadLocalRandom.current().nextInt(0, 100000);

        // Format as prefix-NUMERIC
        return prefix + "-" + timestamp + String.format("%05d", random);
    }
}
