package com.codeshare.airline.inbound.dto.schedule;

import com.codeshare.airline.core.dto.audit.CSMAuditableDTO;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SchedulePeriodDTO extends CSMAuditableDTO {

    /* =========================
       CORE PERIOD
       ========================= */

    private LocalDate startDate;
    private LocalDate endDate;

    /* =========================
       OPERATION
       ========================= */

    private String daysOfOperation;   // 1234567
    private Integer frequencyRate;

    /* =========================
       HELPERS
       ========================= */

    public boolean isValidPeriod() {
        return startDate != null &&
                endDate != null &&
                !startDate.isAfter(endDate);
    }

    public boolean hasDays() {
        return daysOfOperation != null && !daysOfOperation.isBlank();
    }

    public boolean isValidDays() {
        return hasDays() && daysOfOperation.matches("^[1-7]{1,7}$");
    }

    public boolean isValidFrequency() {
        return frequencyRate == null || frequencyRate > 0;
    }
}