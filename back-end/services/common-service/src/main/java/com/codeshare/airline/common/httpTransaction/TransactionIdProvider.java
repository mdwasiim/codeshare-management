package com.codeshare.airline.common.httpTransaction;


public final class TransactionIdProvider {
    private static final ThreadLocal<String> TXN = new ThreadLocal<>();

    private TransactionIdProvider() {}

    public static void set(String id) {
        TXN.set(id);
    }

    public static String get() {
        return TXN.get();
    }

    public static void clear() {
        TXN.remove();
    }
}
