package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantIngestionChannelDTO extends CSMAuditableDTO {

    private MessageType messageType;
    private SourceType sourceType;
    private String partnerCode;
    private Boolean enabled;
    private Integer priority;
    private String host;
    private Integer port;
    private String username;
    private String passwordEncrypted;
    private String remoteDirectory;
    private String protocol;
    private String mailbox;
    private Integer mailDelayMs;
    private Boolean mailUnseenOnly;
    private Boolean mailDelete;
    private Boolean mailPeek;
    private String mailMoveTo;
    private String brokerUrl;
    private String queueName;
    private String topicName;
    private Integer concurrentConsumers;
    private Integer maxConcurrentConsumers;
    private Boolean asyncConsumer;
    private Integer receiveTimeoutMs;
    private Integer maxMessagesPerTask;
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
    private Boolean disconnect;
    private Boolean binary;
    private Boolean passiveMode;
    private Integer reconnectDelayMs;
    private Integer maximumReconnectAttempts;
    private Integer soTimeoutMs;
    private Integer retryAttempts;
    private Integer retryDelayMs;
    private Integer connectionTimeoutMs;
    private Integer readTimeoutMs;
}
