package com.codeshare.airline.common.services.response;


public class RequestTimeProvider {

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    public static void start() {
        START_TIME.set(System.currentTimeMillis());
    }

    public static long getTimeTaken() {
        Long start = START_TIME.get();
        if (start == null) return 0;
        return System.currentTimeMillis() - start;
    }

    public static void clear() {
        START_TIME.remove();
    }
}
