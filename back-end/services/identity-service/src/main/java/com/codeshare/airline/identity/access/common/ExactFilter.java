package com.codeshare.airline.identity.access.common;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class ExactFilter {

    private static final Set<String> RESERVED_FILTER_PARAMS = Set.of(
            "page",
            "size",
            "sort",
            "direction",
            "_",
            "_dc"
    );

    private ExactFilter() {
    }

    public static <T> List<T> apply(List<T> rows, Map<String, String> filters) {
        Map<String, String> effectiveFilters = effectiveFilters(filters);

        if (effectiveFilters.isEmpty()) {
            return rows;
        }

        return rows.stream()
                .filter(row -> matches(row, effectiveFilters))
                .toList();
    }

    private static Map<String, String> effectiveFilters(Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) {
            return Map.of();
        }

        return filters.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .filter(entry -> !entry.getKey().isBlank())
                .filter(entry -> !RESERVED_FILTER_PARAMS.contains(entry.getKey()))
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> !entry.getValue().isBlank())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().trim(),
                        (first, second) -> second
                ));
    }

    private static boolean matches(Object row, Map<String, String> filters) {
        return filters.entrySet().stream()
                .allMatch(entry -> valueAtPath(row, entry.getKey())
                        .map(value -> valueMatches(value, entry.getValue()))
                        .orElse(true));
    }

    private static Optional<Object> valueAtPath(Object row, String fieldPath) {
        Object currentValue = row;

        for (String fieldName : fieldPath.split("\\.")) {
            if (currentValue == null || fieldName.isBlank()) {
                return Optional.empty();
            }

            Optional<Field> field = findField(currentValue.getClass(), fieldName);
            if (field.isEmpty()) {
                return Optional.empty();
            }

            try {
                field.get().setAccessible(true);
                currentValue = field.get().get(currentValue);
            } catch (IllegalAccessException ignored) {
                return Optional.empty();
            }
        }

        return Optional.ofNullable(currentValue);
    }

    private static Optional<Field> findField(Class<?> type, String fieldName) {
        Class<?> current = type;

        while (current != null && current != Object.class) {
            try {
                return Optional.of(current.getDeclaredField(fieldName));
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }

        return Optional.empty();
    }

    private static boolean valueMatches(Object value, String expected) {
        if (value == null) {
            return false;
        }

        return String.valueOf(value).trim().toLowerCase(Locale.ROOT)
                .equals(expected.trim().toLowerCase(Locale.ROOT));
    }
}
