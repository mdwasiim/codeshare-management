package com.codeshare.airline.schedule.ingestion.persistence.entities.schedule;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule_message",
        indexes = {
                @Index(name = "idx_sch_msg_file", columnList = "file_id"),
                @Index(name = "idx_sch_msg_reference", columnList = "message_reference")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleMessageEntity extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private ScheduleFileMetaDataEntity file;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 10)
    private MessageType messageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_mode", length = 5)
    private TimeMode timeMode;

    @Column(name = "sender", length = 10)
    private String sender;

    @Column(name = "recipient", length = 10)
    private String recipient;

    @Column(name = "source", length = 10)
    private String source;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "creation_date_raw", length = 20)
    private String creationDateRaw;

    @Column(name = "creation_time", length = 10)
    private String creationTime;

    @Column(name = "message_reference", length = 50)
    private String messageReference;

    @Column(name = "creator_reference", length = 50)
    private String creatorReference;

    @Column(name = "raw_header", columnDefinition = "TEXT")
    private String rawHeader;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", length = 20)
    private ProcessingStatus processingStatus;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("messageSequenceNumber ASC")
    private List<ScheduleSubMessageEntity> subMessages = new ArrayList<>();


    /* ================= HELPERS ================= */

    public void addSubMessage(ScheduleSubMessageEntity sub) {
        if (sub != null) {
            subMessages.add(sub);
            sub.setMessage(this);
        }
    }
}