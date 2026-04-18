package com.codeshare.airline.inbound.dto.source;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineIngestionChannelDTO extends CSMAuditableDTO {

    /* ======================================================
       CORE
       ====================================================== */

    private MessageType messageType;
    private SourceType sourceType;

    private Boolean enabled;
    private Integer priority;

    /* ======================================================
       CONNECTION (COMMON)
       ====================================================== */

    private String host;
    private Integer port;
    private String username;
    private String passwordEncrypted;

    /* ======================================================
       PATH / DIRECTORY
       ====================================================== */

    private String remoteDirectory;

    /* ======================================================
       EMAIL
       ====================================================== */

    private String protocol;
    private String mailbox;

    private Integer mailDelayMs;
    private Boolean mailUnseenOnly;
    private Boolean mailDelete;
    private Boolean mailPeek;
    private String mailMoveTo;

    /* ======================================================
       MQ
       ====================================================== */

    private String brokerUrl;
    private String queueName;
    private String topicName;

    private Integer concurrentConsumers;
    private Integer maxConcurrentConsumers;
    private Boolean asyncConsumer;
    private Integer receiveTimeoutMs;
    private Integer maxMessagesPerTask;

    /* ======================================================
       FILE / SFTP COMMON
       ====================================================== */

    private Boolean fileNoop;
    private Boolean fileDelete;

    private String fileIncludePattern;
    private String fileExcludePattern;

    private String fileReadLock;
    private String fileReadLockMinAge;
    private Integer fileReadLockTimeout;
    private Integer fileReadLockCheckInterval;

    private Integer filePollDelayMs;
    private Integer fileInitialDelayMs;

    private String fileMove;
    private String fileMoveFailed;
    private String filePreMove;

    private Boolean fileIdempotent;
    private String fileIdempotentKey;

    private String fileCharset;

    private Integer maxMessagesPerPoll;
    private Boolean recursive;
    private Boolean bridgeErrorHandler;

    /* ======================================================
       SFTP ADVANCED
       ====================================================== */

    private Boolean disconnect;
    private Boolean binary;
    private Boolean passiveMode;
    private Integer reconnectDelayMs;
    private Integer maximumReconnectAttempts;
    private Integer soTimeoutMs;

    /* ======================================================
       RETRY / CONTROL
       ====================================================== */

    private Integer retryAttempts;
    private Integer retryDelayMs;

    /* ======================================================
       LEGACY / OPTIONAL (if still used)
       ====================================================== */

    private Integer connectionTimeoutMs;
    private Integer readTimeoutMs;
}
