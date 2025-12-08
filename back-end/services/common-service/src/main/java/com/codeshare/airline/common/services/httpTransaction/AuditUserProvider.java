package com.codeshare.airline.common.services.httpTransaction;


/**
 * Lightweight thread-local holder for "current user" used by audit listener.
 * Microservices can set this at authentication filter / controller advice time.
 */
public final class AuditUserProvider {
    private static final ThreadLocal<String> USER = new ThreadLocal<>();

    private AuditUserProvider() {}

    public static void set(String username) {
        USER.set(username);
    }

    public static String get() {
        return USER.get();
    }

    public static void clear() {
        USER.remove();
    }
}