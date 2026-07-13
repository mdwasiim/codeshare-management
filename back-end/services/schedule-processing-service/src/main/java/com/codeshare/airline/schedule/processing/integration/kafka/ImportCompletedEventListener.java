package com.codeshare.airline.schedule.processing.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ImportCompletedEvent;
import com.codeshare.airline.schedule.processing.application.ScheduleComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportCompletedEventListener {

    private final ScheduleComparisonService scheduleComparisonService;

    @KafkaListener(
            topics = "${schedule.workflow.topics.import-completed}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onImportCompleted(ImportCompletedEvent event) {
        log.info("Received import-completed event importBatchId={} type={}", event.getImportBatchId(), event.getMessageType());
        scheduleComparisonService.compare(event);
    }
}
