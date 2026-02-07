📦 Platform Kafka Service
Centralized Kafka Infrastructure Service

Retries · DLT · Replay · Idempotency · Tracing · Schema Registry

1. Overview

platform-kafka is a Spring Boot–based Kafka infrastructure service that provides enterprise-grade reliability and observability for event-driven systems.

It centralizes all non-business Kafka concerns so that downstream services can focus purely on business logic.

This service is responsible for:

Non-blocking retries using Retry Topics

Dead Letter Topics (DLT)

Manual and controlled DLT Replay

Idempotent consumption (exactly-once effect)

Distributed tracing using Kafka headers + MDC

Exception classification (retry vs non-retry)

Schema Registry integration (Avro)

Central Kafka configuration and standards

2. What This Service Is (and Is Not)
   ✅ This service IS:

A running Spring Boot service

A Kafka infrastructure & recovery platform

Owned by Platform / Integration team

Used by all business services

❌ This service is NOT:

A business-domain service

A place for domain logic

A per-topic consumer for business events

3. Architecture Overview
   ┌────────────────────┐
   │ Business Service A │
   │  (Producer)        │
   └─────────┬──────────┘
   │
   ▼
   Kafka Topics
   │
   ▼
   ┌────────────────────┐
   │  platform-kafka    │
   │--------------------│
   │ Retry Topics       │
   │ DLT                │
   │ DLT Replay         │
   │ Idempotency Store  │
   │ Tracing (MDC)      │
   └────────────────────┘
   │
   ▼
   ┌────────────────────┐
   │ Business Service B │
   │  (Consumer)        │
   └────────────────────┘

4. Kafka Topic Strategy

For a base topic:

order.created

Retry Topics (auto-managed)
order.created-retry-5000
order.created-retry-10000
order.created-retry-20000

Dead Letter Topic
order.created.DLT


⚠️ Retry topics are auto-managed by Spring Kafka
⚠️ DLT topics should be pre-created in production

5. Retry Topics (Non-Blocking Retries)
   Why Retry Topics?

Traditional retries block consumer threads and partitions.
Retry Topics provide asynchronous retries using Kafka itself.

Retry Configuration
Setting	Value
Strategy	Exponential backoff
Initial delay	5 seconds
Multiplier	2.0
Max delay	2 minutes
Max attempts	4 (1 original + 3 retries)
Retry Flow
order.created
↓
order.created-retry-5000
↓
order.created-retry-10000
↓
order.created-retry-20000
↓
order.created.DLT

6. Exception Classification

Retry behavior is driven by exception type, not guesswork.

Exception Types
Exception	Behavior
RetryableKafkaException	Retry Topics
RuntimeException	Retry Topics
NonRetryableKafkaException	Direct DLT
IllegalArgumentException	Direct DLT
Developer Guidance

Throw RetryableKafkaException for:

Network timeouts

Temporary DB issues

Throw NonRetryableKafkaException for:

Validation failures

Invalid payloads

Business rule violations

7. Dead Letter Topic (DLT)
   What goes to DLT?

Non-retryable exceptions

Messages that fail all retry attempts

Corrupt or invalid payloads

DLT Message Headers

Each DLT record includes metadata:

kafka_dlt-original-topic
kafka_dlt-original-partition
kafka_dlt-original-offset
kafka_dlt-exception-message
kafka_dlt-exception-stacktrace


These headers are critical for debugging and replay.

8. DLT Replay (Controlled Recovery)
   What is Replay?

Replay means re-sending a message from <topic>.DLT back to:

the original topic, or

a custom topic

Replay Rules

✅ Replay only after fixing root cause
❌ Never auto-replay
❌ Never replay invalid payloads

Typical Replay Flow

Identify DLT message

Inspect headers + payload

Fix bug/config/dependency

Replay message

Monitor consumer logs

9. Idempotency (Exactly-Once Effect)

Kafka guarantees at-least-once delivery.
This service ensures exactly-once processing effect.

How It Works

Each event has an idempotency key

Stored in a database table

Duplicate messages are skipped safely

Idempotency Table
CREATE TABLE kafka_processed_events (
idempotency_key VARCHAR(128) PRIMARY KEY,
processed_at TIMESTAMP NOT NULL
);

Why This Matters

Safe retries

Safe DLT replay

Safe consumer restarts

Safe parallel processing

10. Transactions (CRITICAL)

All Kafka consumers must be transactional:

@Transactional
protected void handle(Event event) {
// business DB operations
}


This ensures:

Business data

Idempotency record

commit together or rollback together.

11. Distributed Tracing
    Kafka Headers Used
    Header	Purpose
    X-Trace-Id	Distributed trace
    X-Tenant-Id	Tenant context
    X-Source	Producer service
    MDC Logging

All logs automatically include:

traceId=<value> tenantId=<value>


This enables:

End-to-end traceability

Faster incident debugging

12. Schema Registry (Avro)

This service supports:

Avro serialization

Confluent Schema Registry

Schema compatibility enforcement

When Avro is Used

Strong schema governance

Backward/forward compatibility

Cross-team event contracts

13. Configuration
    Required External Dependencies

Kafka brokers

Database (for idempotency)

Schema Registry (if Avro used)

Config Server (Spring Cloud Config)

Example Properties
spring:
kafka:
bootstrap-servers: localhost:9092
datasource:
url: jdbc:postgresql://localhost:5432/kafka
username: kafka
password: kafka

14. Health & Operations
    Health Checks

Kafka connectivity

Database availability

Schema Registry reachability

Common Alerts

Growing DLT size

High retry volume

Consumer lag

DB constraint violations

15. What NOT To Do (Important)

❌ Do not auto-replay DLT
❌ Do not delete retry topics manually
❌ Do not truncate idempotency table without analysis
❌ Do not bypass exception classification

16. Ownership & Escalation
    Owned By

Platform / Integration Team

Escalate To Development When

Payload schema issues

Business rule violations

Domain validation failures

17. Summary

platform-kafka ensures:

✔ No message loss
✔ Controlled retries
✔ Safe recovery
✔ Exactly-once processing
✔ Full observability

Business services focus on logic.
platform-kafka guarantees reliability.