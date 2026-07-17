package com.codeshare.airline.schedule.ingestion.persistence.entities.schedule;

import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "schedule_sub_message",
        indexes = {
                @Index(name = "idx_sch_sub_msg_action", columnList = "action_type")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_message_sequence",
                        columnNames = {"message_id", "message_sequence_number"}
                )
        }
)
public class ScheduleSubMessageEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private ScheduleMessageEntity message;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 20, nullable = false)
    private ActionType actionType;

    @Column(name = "message_sequence_number", nullable = false)
    private Integer messageSequenceNumber;

    @Column(name = "message_reference", length = 50)
    private String messageReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_mode", length = 5)
    private TimeMode timeMode;

    @Lob
    @Column(name = "raw_message", columnDefinition = "TEXT")
    private String rawMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", length = 20)
    private ProcessingStatus processingStatus;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @OneToMany(mappedBy = "subMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("flightSequenceNumber ASC")
    @BatchSize(size = 100)
    private List<ScheduleFlightEntity> flights = new ArrayList<>();

    /* ================= HELPERS ================= */

    public void addFlight(ScheduleFlightEntity flight) {
        if (flight != null) {
            flights.add(flight);
            flight.setSubMessage(this);
        }
    }
}
