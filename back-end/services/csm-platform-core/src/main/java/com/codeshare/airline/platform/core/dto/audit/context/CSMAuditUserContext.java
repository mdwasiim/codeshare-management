package com.codeshare.airline.platform.core.dto.audit.context;

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