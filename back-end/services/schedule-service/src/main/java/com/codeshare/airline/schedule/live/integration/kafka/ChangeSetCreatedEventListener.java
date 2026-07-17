package com.codeshare.airline.schedule.live.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ChangeSetCreatedEvent;
import com.codeshare.airline.schedule.live.application.ScheduleChangeRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeSetCreatedEventListener {

    private final ScheduleChangeRequestService scheduleChangeRequestService;

    @KafkaListener(
            topics = "${schedule.workflow.topics.change-set-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onChangeSetCreated(ChangeSetCreatedEvent event) {
        log.info("Received change-set-created event changeSetId={} airline={}", event.getChangeSetId(), event.getAirlineCode());
        scheduleChangeRequestService.register(event);
    }
}
