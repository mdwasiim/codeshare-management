package com.codeshare.airline.ingestion.persistence.entities.source;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.domain.enums.SourceType;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "schedule_ingestion_channel",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_profile_msg_source",
                        columnNames = {"profile_id", "MESSAGE_TYPE", "source_type"}
                )
        },
        indexes = {
                @Index(name = "idx_channel_profile", columnList = "profile_id"),
                @Index(name = "idx_channel_host", columnList = "host"),
                @Index(name = "idx_channel_queue", columnList = "queue_name"),
                @Index(name = "idx_channel_topic", columnList = "topic_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleIngestionChannelEntity extends CSMDataAbstractEntity {

    /* ======================================================
       CORE
       ====================================================== */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    @NotNull
    private ScheduleIngestionProfileEntity profile;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 20, nullable = false)
    private MessageType messageType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 20, nullable = false)
    private SourceType sourceType;

    /* ======================================================
       COMMON CONNECTION
       ====================================================== */

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private Integer port;

    @Column(name = "username")
    private String username;

    @Column(name = "password_encrypted")
    private String passwordEncrypted;

    /* ======================================================
       DIRECTORY / PATH
       ====================================================== */

    @Column(name = "remote_directory")
    private String remoteDirectory;

    /* ======================================================
       EMAIL
       ====================================================== */

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "mailbox")
    private String mailbox;

    @Column(name = "mail_delay_ms")
    private Integer mailDelayMs;

    @Column(name = "mail_unseen_only")
    private Boolean mailUnseenOnly;

    @Column(name = "mail_delete")
    private Boolean mailDelete;

    @Column(name = "mail_peek")
    private Boolean mailPeek;

    @Column(name = "mail_move_to")
    private String mailMoveTo;

    /* ======================================================
       MQ
       ====================================================== */

    @Column(name = "broker_url")
    private String brokerUrl;

    @Column(name = "queue_name")
    private String queueName;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "concurrent_consumers")
    private Integer concurrentConsumers;

    @Column(name = "max_concurrent_consumers")
    private Integer maxConcurrentConsumers;

    @Column(name = "async_consumer")
    private Boolean asyncConsumer;

    @Column(name = "receive_timeout_ms")
    private Integer receiveTimeoutMs;

    @Column(name = "max_messages_per_task")
    private Integer maxMessagesPerTask;

    /* ======================================================
       FILE / SFTP COMMON
       ====================================================== */

    @Column(name = "file_noop")
    private Boolean fileNoop;

    @Column(name = "file_delete")
    private Boolean fileDelete;

    @Column(name = "file_include_pattern")
    private String fileIncludePattern;

    @Column(name = "file_exclude_pattern")
    private String fileExcludePattern;

    @Column(name = "file_read_lock")
    private String fileReadLock;

    @Column(name = "file_read_lock_min_age")
    private String fileReadLockMinAge;

    @Column(name = "file_read_lock_timeout")
    private Integer fileReadLockTimeout;

    @Column(name = "file_read_lock_check_interval")
    private Integer fileReadLockCheckInterval;

    @Column(name = "file_poll_delay_ms")
    private Integer filePollDelayMs;

    @Column(name = "file_initial_delay_ms")
    private Integer fileInitialDelayMs;

    @Column(name = "file_move")
    private String fileMove;

    @Column(name = "file_move_failed")
    private String fileMoveFailed;

    @Column(name = "file_pre_move")
    private String filePreMove;

    @Column(name = "file_idempotent")
    private Boolean fileIdempotent;

    @Column(name = "file_idempotent_key")
    private String fileIdempotentKey;

    @Column(name = "file_charset")
    private String fileCharset;

    @Column(name = "max_messages_per_poll")
    private Integer maxMessagesPerPoll;

    @Column(name = "is_recursive")
    private Boolean recursive;

    @Column(name = "bridge_error_handler")
    private Boolean bridgeErrorHandler;

    /* ======================================================
       SFTP ADVANCED
       ====================================================== */

    @Column(name = "disconnect_flag")
    private Boolean disconnect;

    @Column(name = "is_binary")   //  FIXED
    private Boolean binary;

    @Column(name = "passive_mode")
    private Boolean passiveMode;

    @Column(name = "reconnect_delay_ms")
    private Integer reconnectDelayMs;

    @Column(name = "max_reconnect_attempts")
    private Integer maximumReconnectAttempts;

    @Column(name = "so_timeout_ms")
    private Integer soTimeoutMs;

    /* ======================================================
       RETRY / CONTROL
       ====================================================== */

    @Column(name = "retry_attempts")
    private Integer retryAttempts;

    @Column(name = "retry_delay_ms")
    private Integer retryDelayMs;

    @Column(name = "is_enabled")
    private Boolean enabled;

    @Column(name = "priority")
    private Integer priority;

    /* ======================================================
       AUDIT
       ====================================================== */

    @Version
    @Column(name = "version")
    private Long version;
}