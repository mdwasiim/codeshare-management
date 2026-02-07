package com.codeshare.airline.processor.config;

import com.codeshare.airline.processor.domain.event.SsimFlightBlockReceivedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, SsimFlightBlockReceivedEvent> consumerFactory() {

        JsonDeserializer<SsimFlightBlockReceivedEvent> valueDeserializer =
                new JsonDeserializer<>(SsimFlightBlockReceivedEvent.class);

        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer,
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false
                ),
                new StringDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SsimFlightBlockReceivedEvent>
    kafkaListenerContainerFactory(
            ConsumerFactory<String, SsimFlightBlockReceivedEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, SsimFlightBlockReceivedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3); // scale safely
        factory.getContainerProperties().setAckMode(
                org.springframework.kafka.listener.ContainerProperties.AckMode.RECORD
        );

        return factory;
    }
}

