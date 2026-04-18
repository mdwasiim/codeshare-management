package com.codeshare.airline.master.messaging.eitities;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.scheduling.eitities.ScheduleFlight;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(
        name = "SCHEDULE_MESSAGE",
        indexes = {
                @Index(name = "IDX_MSG_TYPE", columnList = "SCHEDULE_TYPE"),
                @Index(name = "IDX_MSG_SERIAL", columnList = "MESSAGE_SERIAL_NUMBER")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleMessage extends CSMDataAbstractEntity {

    // ---------------------------------------------------------
    // SSIM HEADER FIELDS (Chapter 4 & 5)
    // ---------------------------------------------------------

    @Column(name = "STANDARD_MESSAGE_IDENTIFIER", length = 10)
    private String standardMessageIdentifier; // e.g. SSM, ASM

    @Column(name = "MESSAGE_TYPE", nullable = false, length = 10)
    private MessageType messageType; // SSM / ASM

    @Column(name = "MESSAGE_VERSION_NUMBER", length = 5)
    private String messageVersionNumber;

    @Column(name = "MESSAGE_SERIAL_NUMBER", length = 20)
    private String messageSerialNumber;

    @Column(name = "MESSAGE_GROUP_SERIAL_NUMBER", length = 20)
    private String messageGroupSerialNumber;

    @Column(name = "CONTINUATION_INDICATOR", length = 1)
    private String continuationIndicator;

    @Column(name = "CREATOR_REFERENCE", length = 50)
    private String creatorReference;

    @Column(name = "CREATION_DATE")
    private LocalDate creationDate;

    @Column(name = "CREATION_TIME")
    private LocalTime creationTime;

    @Column(name = "TRANSMISSION_TIMESTAMP")
    private LocalDateTime transmissionTimestamp;

    @Column(name = "TIME_MODE", length = 10)
    private String timeMode; // UTC / LOCAL

    // ---------------------------------------------------------
    // ENGINE PROCESSING FIELDS
    // ---------------------------------------------------------

    @Column(name = "PROCESSING_STATUS", length = 20)
    private String processingStatus; // PARSED / VALIDATED / FAILED / APPLIED

    @Column(name = "MESSAGE_SOURCE", length = 50)
    private String messageSource;

    // ---------------------------------------------------------
    // RELATIONSHIP
    // ---------------------------------------------------------

    @OneToMany(mappedBy = "scheduleMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleFlight> flights;

    // ---------------------------------------------------------
    // RECORD STATUS
    // ---------------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;
}