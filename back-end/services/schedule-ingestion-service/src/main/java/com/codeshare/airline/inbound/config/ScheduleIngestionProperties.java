package com.codeshare.airline.inbound.config;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "ingestion")
@Getter
@Setter
public class ScheduleIngestionProperties {

    private SourceType sourceType;

    private Map<MessageType, List<String>> allowedExtensions;

}