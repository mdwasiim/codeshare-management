package com.codeshare.airline.core.dto.audit.context;


/**
 * Lightweight thread-local holder for "current user" used by audit listener.
 * Microservices can set this at authentication filter / controller advice time.
 */
public final class CSMAuditUserContext {
    private static final ThreadLocal<String> USER = new ThreadLocal<>();

    private CSMAuditUserContext() {}

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