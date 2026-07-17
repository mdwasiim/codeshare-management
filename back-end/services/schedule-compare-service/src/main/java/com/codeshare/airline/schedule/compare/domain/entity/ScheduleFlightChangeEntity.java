package com.codeshare.airline.schedule.compare.domain.entity;

import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.compare.domain.enums.ChangeSetStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule_flight_change",
        schema = "schedule_compare",
        indexes = {
                @Index(name = "idx_sfc_change_set", columnList = "change_set_id"),
                @Index(name = "idx_sfc_airline_flt", columnList = "airline_code, flight_number"),
                @Index(name = "idx_sfc_change_set_status", columnList = "change_set_status")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sfc_change_set_flight",
                        columnNames = {
                                "change_set_id",
                                "airline_code",
                                "flight_number",
                                "operational_suffix",
                                "itinerary_variation_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ScheduleFlightChangeEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "change_set_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_sfc_change_set")
    )
    private ChangeSetEntity changeSet;

    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @Column(name = "flight_number", length = 4, nullable = false)
    private String flightNumber;

    @Column(name = "operational_suffix", length = 1)
    private String operationalSuffix;

    @Column(name = "itinerary_variation_id", length = 2)
    private String itineraryVariationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_set_status", length = 20, nullable = false)
    private ChangeSetStatus changeSetStatus;

    @Column(name = "status_recorded_at")
    private Instant statusRecordedAt;

    @Column(name = "status_reason", columnDefinition = "TEXT")
    private String statusReason;

    @OneToMany(
            mappedBy = "flightChange",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("legSequenceNumber ASC")
    @Builder.Default
    private List<ScheduleLegChangeEntity> legChanges = new ArrayList<>();

    public void addLegChange(ScheduleLegChangeEntity legChange) {
        if (legChange != null) {
            legChanges.add(legChange);
            legChange.setFlightChange(this);
        }
    }
}
