package com.codeshare.airline.schedule.persistence.inbound.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "SCHEDULE_INBOUND_FILE",
        schema = "SCHEDULE_OPERATIONAL",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_SCHEDULE_FILE_ID",
                        columnNames = "FILE_ID"
                )
        },
        indexes = {
                @Index(name = "IDX_SCHEDULE_STATUS", columnList = "STATUS"),
                @Index(name = "IDX_SCHEDULE_MESSAGE_TYPE", columnList = "MESSAGE_TYPE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleInboundFile extends CSMDataAbstractEntity {

    @Column(name = "FILE_ID", nullable = false, length = 64)
    private String fileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", length = 10, nullable = false)
    private ScheduleMessageType messageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE_TYPE", length = 20)
    private ScheduleSourceType sourceType;

    @Column(name = "SOURCE_SYSTEM", length = 50)
    private String sourceSystem;

    @Column(name = "AIRLINE_CODE", length = 3)
    private String airlineCode;

    @Column(name = "FILE_SIZE_BYTES")
    private Long fileSizeBytes;

    @Column(name = "CHECKSUM", length = 128)
    private String checksum;

    @Column(name = "RAW_MESSAGE", columnDefinition = "TEXT")
    private String rawMessage;

    @Column(name = "RECEIVED_AT", nullable = false)
    private Instant receivedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 30)
    private ProcessingStatus processingStatus;

    @Column(name = "FAILED_AT")
    private Instant failedTimestamp;

    @Column(name = "ERROR_MESSAGE", length = 1000)
    private String errorMessage;

    @Version
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(
            mappedBy = "inboundFile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ScheduleInboundBlock> blocks = new ArrayList<>();

    public boolean isCompleted() {
        return processingStatus == ProcessingStatus.COMPLETED;
    }
}