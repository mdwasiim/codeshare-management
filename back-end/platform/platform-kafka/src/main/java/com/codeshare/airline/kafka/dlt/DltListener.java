package com.codeshare.airline.kafka.dlt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DltListener {

    private final DltReplayService replayService;

    @KafkaListener(
            topicPattern = ".*\\.DLT",
            groupId = "kafka-dlt-listener"
    )
    public void listen(ConsumerRecord<String, Object> record) {

        log.error("""
            ☠️ DLT MESSAGE
            Topic      : {}
            Key        : {}
            Payload    : {}
            Headers    : {}
            """,
                record.topic(),
                record.key(),
                record.value(),
                record.headers()
        );

        // ❌ DO NOT AUTO-REPLAY
        // Replay must be explicit (manual / API / command)
    }

    /**
     * Example manual replay hook
     */
    public void replay(ConsumerRecord<String, Object> record) {
        replayService.replayToOriginalTopic(record);
    }
}
