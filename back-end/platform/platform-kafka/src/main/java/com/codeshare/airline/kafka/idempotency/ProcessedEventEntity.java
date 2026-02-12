/*
package com.codeshare.airline.kafka.idempotency;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "kafka_processed_events",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_kafka_idempotency", columnNames = "idempotency_key")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEventEntity {

    @Id
    @Column(name = "idempotency_key", length = 128, nullable = false)
    private String idempotencyKey;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;
}
*/
