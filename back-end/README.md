# Codeshare Management Backend

Spring Boot multi-module backend for Codeshare Management.

## Backend Structure

```text
back-end/
  common/       Shared backend support code
  services/
    gateway-service/
    identity-service/
    master-data-service/
    schedule-ingestion-service/
    schedule-processing-service/
    csm-core/
    csm-data-jpa/
    csm-web/
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
