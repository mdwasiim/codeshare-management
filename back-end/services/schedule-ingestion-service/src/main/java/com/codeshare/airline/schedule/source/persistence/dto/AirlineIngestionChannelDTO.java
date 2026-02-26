package com.codeshare.airline.schedule.source.persistence.dto;

import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AirlineIngestionChannelDTO {

    private UUID id;

    private ScheduleMessageType messageType;
    private ScheduleSourceType sourceType;

    private String host;
    private Integer port;
    private String username;
    private String passwordEncrypted;
    private String remoteDirectory;

    private String protocol;
    private String mailbox;

    private String brokerUrl;
    private String queueName;
    private String topicName;

    private Integer connectionTimeoutMs;
    private Integer readTimeoutMs;
}
