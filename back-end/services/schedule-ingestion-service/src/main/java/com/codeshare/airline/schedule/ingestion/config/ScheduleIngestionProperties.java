package com.codeshare.airline.schedule.ingestion.config;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.domain.enums.SsimValidationMode;
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

    private Ssim ssim = new Ssim();

    @Getter
    @Setter
    public static class Ssim {
        private int flightBatchSize = 5_000;
        private SsimValidationMode validationMode = SsimValidationMode.RELAXED;
    }
}
