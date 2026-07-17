package com.codeshare.airline.schedule.compare.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.CompareRequestedEvent;
import com.codeshare.airline.schedule.compare.application.ScheduleComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompareRequestedEventListener {

    private final ScheduleComparisonService scheduleComparisonService;

    @KafkaListener(
            topics = "${schedule.workflow.topics.compare-requested}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onCompareRequested(CompareRequestedEvent event) {
        log.info("Received compare-requested event importBatchId={} type={}",
                event.getImportBatchId(), event.getMessageType());
        scheduleComparisonService.compare(event);
    }
}
