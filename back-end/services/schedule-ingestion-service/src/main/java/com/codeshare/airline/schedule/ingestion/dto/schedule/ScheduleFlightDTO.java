package com.codeshare.airline.schedule.ingestion.dto.schedule;

import com.codeshare.airline.core.dto.audit.CSMAuditableDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFlightDTO extends CSMAuditableDTO implements ScheduleMessageItem {

    /* ================= IDENTITY ================= */

    private String airlineDesignator;
    private String flightNumber;
    private String operationalSuffix;

    private Integer flightSequenceNumber;

    private String operationDate;

    private String boardPoint;
    private String offPoint;

    /* ================= PERIODS ================= */

    @Builder.Default
    private List<SchedulePeriodDTO> periods = new ArrayList<>();

    public void addPeriod(SchedulePeriodDTO period) {
        periods.add(period);
    }

    /* ================= EQUIPMENT ================= */

    private String aircraftType;
    private String serviceType;
    private String aircraftConfiguration;
    private String bookingDesignator;

    /* ================= LEGS ================= */

    @Builder.Default
    private List<ScheduleLegDTO> legs = new ArrayList<>();

    public void addLeg(ScheduleLegDTO leg) {
        leg.setLegSequenceNumber(legs.size() + 1);
        legs.add(leg);
    }

    /* ================= DEI ================= */

    @Builder.Default
    private List<ScheduleDataElementDTO> deis = new ArrayList<>();

    public void addFlightDei(ScheduleDataElementDTO dei) {
        deis.add(dei);
    }

    /* ================= SUPPLEMENTARY ================= */

    @Builder.Default
    private List<String> supplementaryInfo = new ArrayList<>();

    public List<ScheduleLegDTO> getLegs() {
        return legs != null ? legs : new ArrayList<>();
    }

    public List<ScheduleDataElementDTO> getDeis() {
        return deis != null ? deis : new ArrayList<>();
    }

    public List<String> getSupplementaryInfo() {
        return supplementaryInfo != null ? supplementaryInfo : new ArrayList<>();
    }
}