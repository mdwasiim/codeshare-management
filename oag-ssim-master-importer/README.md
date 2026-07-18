# OAG SSIM Master Importer

Standalone Java CLI for extracting master/reference codes from OAG/IATA SSIM fixed-width files and inserting them into the existing `schedule_master_data` tables used by `master-data-service`.

This project is intentionally not added to the main backend Maven reactor. It does not depend on the current services and writes through JDBC only.

## What It Imports

From SSIM record type 2:

- airline designator -> `AIRLINE_CARRIER`
- season -> `SEASON`
- time mode -> `TIME_MODE`

From SSIM record type 3:

- marketing/operating/onward airline designators -> `AIRLINE_CARRIER`
- departure and arrival stations -> `AIRPORT`
- departure and arrival terminals -> `TERMINAL`
- aircraft type -> `AIRCRAFT_TYPE`
- aircraft owner, cockpit employer, cabin employer -> related aircraft operator tables
- service type -> `SERVICE_TYPE`
- PRBD letters -> `RESERVATION_BOOKING_DESIGNATOR`
- PRBM codes -> `RESERVATION_BOOKING_MODIFIER`
- meal service codes -> `MEAL_SERVICE`
- secure flight indicator -> `SECURE_FLIGHT_INDICATOR`
- operational suffix -> `OPERATIONAL_SUFFIX`
- frequency code -> `FLIGHT_FREQUENCY`
- traffic restriction codes -> `TRAFFIC_RESTRICTION_CODE`

From SSIM record type 4:

- DEI codes -> `DATA_ELEMENT_IDENTIFIER` and `DEI`
- board/off points -> `AIRPORT`

## Important Data Quality Rule

SSIM schedule files often contain codes, not full reference descriptions. For mandatory fields or parent relationships that are not present in SSIM, the importer creates deterministic placeholder parents such as:

- `REGION = SSIM`
- `COUNTRY = ZZ / ZZZ`
- `TIMEZONE = UTC`
- `CITY = SSIM <station>`
- `AIRCRAFT_MANUFACTURER = SSIM`
- `AIRCRAFT_FAMILY = SSIM`

That allows the current schema constraints and relationships to remain valid. These placeholders should later be enriched from authoritative IATA/OAG reference feeds.

## Build

```bash
mvn package
```

## Run Dry-Run

```bash
java -jar target/oag-ssim-master-importer-1.0.0-SNAPSHOT.jar ^
  --ssim-file C:\path\to\OAG.ssim ^
  --dry-run true
```

## Run Import

```bash
java -jar target/oag-ssim-master-importer-1.0.0-SNAPSHOT.jar ^
  --config application.properties ^
  --ssim-file C:\path\to\OAG.ssim ^
  --dry-run false
```

Copy `src/main/resources/application.properties.example` to `application.properties` and set database details.

