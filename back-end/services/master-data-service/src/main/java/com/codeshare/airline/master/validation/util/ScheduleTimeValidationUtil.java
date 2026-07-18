package com.codeshare.airline.master.validation.util;

import com.codeshare.airline.master.geography.entities.Timezone;
import com.codeshare.airline.master.geography.repository.DstRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ScheduleTimeValidationUtil {

    private static final int MAX_REASONABLE_FLIGHT_HOURS = 36;

    private final DstRuleRepository dstRuleRepository;

    public int expectedOffsetMinutes(Timezone timezone, LocalDate date, LocalTime time) {
        if (timezone == null || timezone.getId() == null || timezone.getStandardUtcOffsetMinutes() == null) {
            throw new IllegalArgumentException("Timezone master is incomplete");
        }
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        int standardOffset = timezone.getStandardUtcOffsetMinutes();
        return dstRuleRepository.findActiveDstPeriods(timezone.getId(), localDateTime)
                .stream()
                .findFirst()
                .map(period -> standardOffset + period.getDstOffsetMinutes())
                .orElse(standardOffset);
    }

    public Optional<Integer> expectedDateVariation(LocalDate departureDate,
                                                   LocalTime departureTime,
                                                   int departureOffsetMinutes,
                                                   LocalTime arrivalTime,
                                                   int arrivalOffsetMinutes) {

        LocalDateTime departureLocal = LocalDateTime.of(departureDate, departureTime);
        for (int variation = -1; variation <= 2; variation++) {
            LocalDateTime arrivalLocal = LocalDateTime.of(departureDate.plusDays(variation), arrivalTime);
            Duration duration = Duration.between(
                    departureLocal.toInstant(ZoneOffset.ofTotalSeconds(departureOffsetMinutes * 60)),
                    arrivalLocal.toInstant(ZoneOffset.ofTotalSeconds(arrivalOffsetMinutes * 60))
            );
            if (!duration.isNegative()
                    && !duration.isZero()
                    && duration.compareTo(Duration.ofHours(MAX_REASONABLE_FLIGHT_HOURS)) <= 0) {
                return Optional.of(variation);
            }
        }
        return Optional.empty();
    }

    public int parseDateVariation(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        String normalized = value.trim();
        if (normalized.startsWith("+")) {
            normalized = normalized.substring(1);
        }
        return Integer.parseInt(normalized);
    }

    public int parseOffsetMinutes(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UTC offset is required");
        }
        String normalized = value.trim();
        if (normalized.matches("[+-]\\d{2}:\\d{2}")) {
            normalized = normalized.replace(":", "");
        }
        if (!normalized.matches("[+-]\\d{4}")) {
            throw new IllegalArgumentException("UTC offset must be in +HHMM or +HH:MM format");
        }
        int sign = normalized.charAt(0) == '-' ? -1 : 1;
        int hours = Integer.parseInt(normalized.substring(1, 3));
        int minutes = Integer.parseInt(normalized.substring(3, 5));
        if (hours > 14 || minutes > 59) {
            throw new IllegalArgumentException("UTC offset is outside the valid range");
        }
        return sign * ((hours * 60) + minutes);
    }

    public long dateVariation(LocalDate departureDate, LocalDate arrivalDate) {
        if (departureDate == null || arrivalDate == null) {
            throw new IllegalArgumentException("Departure date and arrival date are required");
        }
        return Duration.between(departureDate.atStartOfDay(), arrivalDate.atStartOfDay()).toDays();
    }

    public String formatOffset(int minutes) {
        String sign = minutes < 0 ? "-" : "+";
        int absolute = Math.abs(minutes);
        return sign + String.format("%02d%02d", absolute / 60, absolute % 60);
    }
}
