package com.codeshare.airline.schedule.source.persistence.entity;

import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRLINE_INGESTION_CHANNEL",
        schema = "SCHEDULE_MASTER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_PROFILE_MSG_SOURCE",
                        columnNames = {
                                "PROFILE_ID",
                                "MESSAGE_TYPE",
                                "SOURCE_TYPE"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CHANNEL_PROFILE", columnList = "PROFILE_ID"),
                @Index(name = "IDX_CHANNEL_HOST", columnList = "HOST"),
                @Index(name = "IDX_CHANNEL_QUEUE", columnList = "QUEUE_NAME"),
                @Index(name = "IDX_CHANNEL_TOPIC", columnList = "TOPIC_NAME")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineIngestionChannel extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "PROFILE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CHANNEL_PROFILE")
    )
    @NotNull
    private AirlineIngestionProfile profile;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", length = 20, nullable = false)
    private ScheduleMessageType messageType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE_TYPE", length = 20, nullable = false)
    private ScheduleSourceType sourceType;

    /* ---------- SFTP ---------- */

    @Size(max = 150)
    @Column(name = "HOST", length = 150)
    private String host;

    @Min(1)
    @Max(65535)
    @Column(name = "PORT")
    private Integer port;

    @Size(max = 150)
    @Column(name = "USERNAME", length = 150)
    private String username;

    @Size(max = 512)
    @Column(name = "PASSWORD_ENCRYPTED", length = 512)
    private String passwordEncrypted;

    @Size(max = 255)
    @Column(name = "REMOTE_DIRECTORY", length = 255)
    private String remoteDirectory;

    /* ---------- EMAIL ---------- */

    @Size(max = 20)
    @Column(name = "PROTOCOL", length = 20)
    private String protocol;

    @Size(max = 100)
    @Column(name = "MAILBOX", length = 100)
    private String mailbox;

    /* ---------- MQ ---------- */

    @Size(max = 500)
    @Column(name = "BROKER_URL", length = 500)
    private String brokerUrl;

    @Size(max = 150)
    @Column(name = "QUEUE_NAME", length = 150)
    private String queueName;

    @Size(max = 150)
    @Column(name = "TOPIC_NAME", length = 150)
    private String topicName;

    @PositiveOrZero
    @Column(name = "CONNECTION_TIMEOUT_MS")
    private Integer connectionTimeoutMs;

    @PositiveOrZero
    @Column(name = "READ_TIMEOUT_MS")
    private Integer readTimeoutMs;

    @Version
    @Column(name = "VERSION")
    private Long version;
}