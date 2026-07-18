package com.codeshare.airline.distribution.engine.integration.kafka;

import com.codeshare.airline.distribution.engine.application.DistributionEngineService;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistributionRequestedEventListener {

    private final DistributionEngineService distributionEngineService;

    @KafkaListener(
            topics = "${distribution.workflow.topics.distribution-requested}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onDistributionRequested(DistributionRequestedEvent event) {
        log.info("Received distribution request changeSetId={} airline={} partner={}",
                event.getChangeSetId(),
                event.getAirlineCode(),
                event.getPartnerCode());
        distributionEngineService.process(event);
    }
}
