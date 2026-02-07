package com.codeshare.airline.processor.reliability.dlt;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsimDltReplayService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void replay(ConsumerRecord<String, Object> record, String targetTopic) {

        kafkaTemplate.send(
                targetTopic,
                record.key(),
                record.value()
        );
    }
}
