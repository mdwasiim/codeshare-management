# IATA SSIM Chapter 4, 5, and 7 Compliance Matrix

This matrix tracks the implemented system behavior against the project-approved IATA scope. It is an engineering control document: every new parser, validator, generator, or reference-data rule should update this file.

## Scope

| Chapter | Area | Owning service | Status |
| --- | --- | --- | --- |
| 4 | SSIM full-schedule ingestion, record sequencing, structural validation, parsed schedule storage | schedule-ingestion-service | Implemented for current parsed model |
| 5 | Operating flight, leg, segment, DEI, codeshare, traffic restriction, terminals, time/UTC data | schedule-ingestion-service, schedule-processing-service, master-data-service, schedule-message-service | Implemented for supported fields |
| 7 | SSM/ASM change actions, outbound generation, action preservation, acceptance/distribution flow | schedule-compare-service, schedule-service, schedule-message-service, distribution-engine-service | Implemented for current change-set model |

## Implemented Controls

| Control | Implementation | Verification |
| --- | --- | --- |
| Raw schedule message is preserved before processing | schedule-ingestion-service import workflow | Module tests and workflow smoke script |
| Structural validation happens before parsing completion | schedule-ingestion-service validators | Existing ingestion regression tests |
| Business validation is owned by schedule-processing-service | processing workflow and processing-requested event boundary | Existing workflow tests |
| Compare is isolated from processing | schedule-compare-service compare-requested consumer | Existing compare tests |
| Live schedule ownership is isolated | schedule-service applies approved/auto changes only | Existing schedule-service tests |
| Outbound SSM, ASM, SSIM generation is centralized | schedule-message-service generator | ScheduleMessageServiceTest |
| Partner-level acceptance is used | schedule-compare-service partner acceptance rule lookup | Existing compare tests |
| Partner-level distribution is used | distribution-engine-service profile resolution | DistributionEngineServiceTest |
| Standard action identifiers are preserved | NEW, CNL, TIM, EQT, RIN, REV, COD, FLT mapping | ScheduleMessageServiceTest |
| Master-data code-list validation is enforced before outbound | schedule-message-service calls master-data-service | ScheduleMessageServiceTest |
| UTC offset/date variation validation uses timezone and DLS masters | master-data-service schedule-time validation | Targeted module tests and smoke script endpoint |
| Correlation and causation IDs are propagated | workflow events and outbound adapter headers/properties | Module tests and log inspection |
| Kafka retry/DLT is configured on consumers | service KafkaRetryConfiguration classes | Module compile and runtime smoke |

## Explicitly Supported Outbound Fields

| Field group | Supported data |
| --- | --- |
| Message type | SSIM, SSM, ASM |
| Flight identity | operating airline, flight number, operational suffix |
| Period/frequency | period start/end, days of operation, frequency rate |
| Leg identity | departure/arrival airport, leg sequence |
| Timing | departure time, arrival time, date variation, UTC offsets |
| Equipment | aircraft type, aircraft configuration |
| Passenger/commercial | service type, RBD, booking modifier, meal services |
| Codeshare | marketing carrier, board/off point, marketing flight number, source DEI |
| DEI | supported master-data DEI list, including codeshare-related DEIs |
| Restrictions | traffic restriction code and qualifier validation |
| Terminals | departure and arrival terminal validation by airport |

## Known Non-Claims

The current implementation does not claim support for every optional IATA field that is not represented in the existing schedule snapshot/change-set model. Adding those fields requires a scoped model extension through ingestion, processing, compare, live schedule, outbound message generation, and distribution.

## Runtime Verification

Run the smoke verifier after the local or test stack is started:

```powershell
.\scripts\verify-schedule-workflow.ps1
```

Run it with a real sample file to trigger ingestion:

```powershell
.\scripts\verify-schedule-workflow.ps1 -SampleFile .\samples\partner-ssm.txt -SampleType SSM -SampleAirlineCode QR
```

The expected business flow is:

```text
schedule-ingestion-service
  -> schedule-processing-service
  -> schedule-compare-service
  -> schedule-service
  -> schedule-message-service
  -> distribution-engine-service
```
