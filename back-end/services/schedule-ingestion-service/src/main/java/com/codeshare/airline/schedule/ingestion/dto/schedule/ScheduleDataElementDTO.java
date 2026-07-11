package com.codeshare.airline.schedule.ingestion.dto.schedule;


import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.schedule.ingestion.domain.enums.DeiScope;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDataElementDTO extends CSMAuditableDTO implements ScheduleMessageItem {

    /* ================= CORE ================= */

    private Integer deiCode;
    private String value;
    private DeiScope scope;
    private Integer sequenceOrder;

    /* ================= LOCATION ================= */

    private Integer legSequenceNumber;
    private String boardPoint;
    private String offPoint;

    /* ================= RAW ================= */

    private String rawLine;

    /* ================= HELPERS ================= */

    public boolean isFlightLevel() {
        return DeiScope.FLIGHT.equals(this.scope);
    }

    public boolean isLegLevel() {
        return DeiScope.LEG.equals(this.scope);
    }

    public boolean isSegmentLevel() {
        return DeiScope.SEGMENT.equals(this.scope);
    }

    /* ================= EQUALITY (IMPORTANT) ================= */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleDataElementDTO that)) return false;

        return Objects.equals(deiCode, that.deiCode) &&
                Objects.equals(value, that.value) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(boardPoint, that.boardPoint) &&
                Objects.equals(offPoint, that.offPoint) &&
                Objects.equals(legSequenceNumber, that.legSequenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deiCode, value, scope, boardPoint, offPoint, legSequenceNumber);
    }
}