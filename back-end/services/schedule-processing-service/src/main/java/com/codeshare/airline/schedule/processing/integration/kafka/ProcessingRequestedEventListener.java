package com.codeshare.airline.schedule.processing.integration.kafka;

import com.codeshare.airline.platform.core.events.schedule.ProcessingRequestedEvent;
import com.codeshare.airline.schedule.processing.application.ScheduleProcessingJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessingRequestedEventListener {

    private final ScheduleProcessingJobService scheduleProcessingJobService;

    @KafkaListener(
            topics = "${schedule.workflow.topics.processing-requested}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onProcessingRequested(ProcessingRequestedEvent event) {
        log.info("Received processing-requested event importBatchId={} type={}",
                event.getImportBatchId(), event.getMessageType());
        scheduleProcessingJobService.process(event);
    }
}
