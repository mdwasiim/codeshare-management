# Schedule Ingestion Service

This service accepts schedule files/messages, extracts message blocks, validates them, parses them, and stores the loaded schedule data.

## Package Map

```text
com.codeshare.airline.schedule.ingestion
├── api             REST controllers, request/response models, API-facing services
├── config          Spring configuration and service properties
├── domain          Context objects, enums, and metadata used by ingestion workflows
├── dto             Parsed schedule/SSIM transfer objects
├── extraction      Input stream extraction and batch helpers
├── orchestration   Pipeline coordination, parsers, handlers, and processors
├── persistence     Database entities, repositories, mappers, and persistence services
├── shared          Shared low-level helpers used inside this service
├── source          Source-channel setup: Camel routes, credentials, route refresh, source models
└── validation      Structural/business validation engine and validators
```

## Flow

```text
source -> extraction -> orchestration parser/processor -> validation -> persistence
```

Keep new code close to the workflow stage it belongs to. For example, a new file/SFTP/MQ source concern belongs under `source`, a parser belongs under `orchestration`, and database write/query logic belongs under `persistence`.
