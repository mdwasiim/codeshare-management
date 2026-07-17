package com.codeshare.airline.schedule.message.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ScheduleUpdatedEvent;
import com.codeshare.airline.schedule.message.application.ScheduleMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleUpdatedEventListener {

    private final ScheduleMessageService scheduleMessageService;

    @KafkaListener(
            topics = "${schedule.workflow.topics.schedule-updated}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onScheduleUpdated(ScheduleUpdatedEvent event) {
        log.info("Received schedule-updated event changeSetId={} type={}",
                event.getChangeSetId(), event.getMessageType());
        scheduleMessageService.generate(event);
    }
}
