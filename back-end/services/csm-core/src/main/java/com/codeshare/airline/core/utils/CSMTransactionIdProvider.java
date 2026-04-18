package com.codeshare.airline.core.utils;

public final class CSMTransactionIdProvider {

    private static final ThreadLocal<String> TXN = new InheritableThreadLocal<>();

    private CSMTransactionIdProvider() {}

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
