package com.codeshare.airline.inbound.dto.schedule;

import com.codeshare.airline.core.dto.audit.CSMAuditableDTO;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleLegDTO extends CSMAuditableDTO implements ScheduleMessageItem {

    /* ================= SEQUENCE ================= */

    private Integer legSequenceNumber;

    /* ================= ROUTING ================= */

    private String boardPoint;
    private String offPoint;

    /* ================= TIMING ================= */

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    // Derived from +1, +2 etc in SSM
    private Integer departureDayOffset;
    private Integer arrivalDayOffset;

    /* ================= EQUIPMENT OVERRIDE ================= */

    private String aircraftType;
    private String aircraftConfiguration;
    private String serviceType;

    /* ================= DEIs ================= */

    @Builder.Default
    private List<ScheduleDataElementDTO> deis = new ArrayList<>();

    public void addLegDei(ScheduleDataElementDTO dei) {
        deis.add(dei);
    }

    /* ================= HELPERS ================= */

    public boolean hasValidTimes() {
        return departureTime != null && arrivalTime != null;
    }

    public boolean isOvernight() {
        return arrivalDayOffset != null && arrivalDayOffset > 0;
    }
}