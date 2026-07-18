# Codeshare Management Backend

Spring Boot multi-module backend for Codeshare Management.

## Backend Structure

```text
back-end/
  common/       Shared backend support code
  services/
    csm-platform-core/
    csm-platform-data-jpa/
    csm-platform-web-starter/
    csm-platform-security/
    gateway-service/
    identity-service/
    master-data-service/
    schedule-ingestion-service/
    schedule-processing-service/
    schedule-service/
```

## Service Layering

Each service should keep code in predictable layers:

```text
controller/     HTTP API entry points
entities/       JPA entities
repository/     Data access
service/        Service interfaces and domain rules
service/impl/   Service implementations
utils/          Small helpers and mappers
```

Keep controllers thin. Put request orchestration and business decisions in services. Keep mapping logic in mapper/helper classes when it grows beyond a few fields.

## Identity Service Notes

`identity-service` owns authentication, authorization data, tenants, users, roles, groups, permissions, menus, and assignment APIs. It is the backend source for the IAM frontend screens.

## Commands

```bash
./mvnw test
./mvnw package
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

## Schedule Workflow Smoke Check

The current Chapter 4/5/7 implementation scope is tracked in
[IATA_CHAPTER_4_5_7_COMPLIANCE_MATRIX.md](docs/IATA_CHAPTER_4_5_7_COMPLIANCE_MATRIX.md).

After the services and infrastructure are running, use the workflow verifier to check service health,
internal S2S authentication, outbound schedule master-data validation, time-validation wiring, and
required schedule workflow topic names:

```powershell
.\scripts\verify-schedule-workflow.ps1
```

The script defaults to the local service ports from each service `application.yml`. Override URLs or
S2S credentials with environment variables such as `IDENTITY_SERVICE_URL`, `MASTER_DATA_SERVICE_URL`,
`INTERNAL_S2S_CLIENT_ID`, and `INTERNAL_S2S_CLIENT_SECRET`.

To trigger an end-to-end sample ingestion after the health and master-data checks, provide a real
schedule message file:

```powershell
.\scripts\verify-schedule-workflow.ps1 -SampleFile .\samples\partner-ssm.txt -SampleType SSM -SampleAirlineCode QR
```
