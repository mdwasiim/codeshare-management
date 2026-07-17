package com.codeshare.airline.schedule.live.domain.entity;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
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
import java.util.UUID;

@Entity
@Table(
        name = "active_schedule",
        schema = "schedule_live",
        indexes = {
                @Index(name = "idx_as_airline", columnList = "airline_code"),
                @Index(name = "idx_as_last_updated", columnList = "last_updated_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_as_airline", columnNames = "airline_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ActiveScheduleEntity extends CSMDataAbstractEntity {

    @Column(name = "airline_code", length = 3, nullable = false)
    private String airlineCode;

    @Column(name = "last_applied_change_set_id")
    private UUID lastAppliedChangeSetId;

    @Column(name = "last_imported_schedule_id")
    private UUID lastImportedScheduleId;

    @Column(name = "last_import_batch_id")
    private UUID lastImportBatchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_message_type", length = 10)
    private MessageType lastMessageType;

    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

    @OneToMany(
            mappedBy = "activeSchedule",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY
    )
    @OrderBy("flightNumber ASC, operationalSuffix ASC, itineraryVariationId ASC")
    @Builder.Default
    private List<LiveFlightEntity> flights = new ArrayList<>();
}
