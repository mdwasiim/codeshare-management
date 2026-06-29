# SSIM Compliance Checklist

This checklist tracks the current SSIM implementation against the ingestion behavior expected for IATA SSIM-style fixed-width files.

It is not a certification statement against a specific IATA SSIM manual version. For formal compliance, validate this checklist against the exact licensed manual/version used by the business.

## Status Legend

| Status | Meaning |
| --- | --- |
| Done | Implemented in parser/persistence/validation. |
| Partial | Basic support exists, but not full manual-level behavior. |
| Missing | Not implemented. |
| Not Required Yet | Known requirement, but not currently needed by UI or downstream processing. |

## File Structure

| Area | Expected Capability | Current Status | Notes |
| --- | --- | --- | --- |
| Fixed-width records | Every non-padding SSIM record is 200 characters | Done | Enforced by `SsimMessageParser` and structural validator. |
| Padding records | Ignore zero-padding records | Done | Extractor skips records starting with `0`. |
| Record types | Support Type 1, 2, 3, 4, 5 | Done | Header, carrier, leg, DEI, trailer are parsed. |
| Multi-airline file | One physical file can contain multiple carrier sections | Done | Extractor emits carrier sections/chunks; persistence creates logical SSIM file per airline/carrier section. |
| Large file chunking | Process large carrier sections without holding the full airline section in memory | Done | Extractor chunks by configurable `ingestion.ssim.flight-batch-size`. |
| Sequence validation | Validate header/carrier/flight/DEI/trailer order | Partial | Basic order validation exists. Chunked SSIM processing relaxes trailer requirement for intermediate chunks. |
| Trailer reconciliation | Validate trailer counts/serial/check references against parsed records | Missing | Needed for stronger manual compliance. |
| Validation mode | Support relaxed bulk ingestion and strict compliance-oriented parsing | Partial | `ingestion.ssim.validation-mode=RELAXED|STRICT` exists. More strict validators are still needed. |

## Type 1 Header

| Field/Rule | Current Status | Notes |
| --- | --- | --- |
| Record type | Done | Parsed and stored as code `1`. |
| Title of contents | Done | Parsed and persisted. |
| Number of seasons | Partial | Numeric validation exists; semantic validation not implemented. |
| Dataset serial number | Partial | Validates 3 digits. No cross-record validation. |
| Record serial number | Partial | Validates 6 digits. No full sequence reconciliation. |
| Spare fields | Done | Preserved as raw fields. |

## Type 2 Carrier

| Field/Rule | Current Status | Notes |
| --- | --- | --- |
| Record type | Done | Parsed and stored as code `2`. |
| Time mode | Partial | Supports `L` and `U`; invalid values fail in strict mode and become null in relaxed mode. |
| Airline code | Partial | Parsed and stored. Format/known carrier validation missing. |
| Season | Partial | Parsed. No semantic season validation. |
| Validity start/end | Partial | Raw dates parsed. Date validity/range validation missing. |
| Creation date/time | Partial | Raw fields parsed. Date/time validation missing. |
| Title of data | Done | Parsed and persisted. |
| Release date | Partial | Raw date parsed. Date validation missing. |
| Schedule status | Partial | Parsed. Allowed-value validation missing. |
| Creator reference | Done | Parsed and persisted. |
| Duplicate designator marker | Partial | Parsed. Allowed-value behavior missing. |
| General information | Done | Parsed and persisted. |
| Inflight service info | Done | Parsed and persisted. |
| Electronic ticketing info | Partial | Parsed. Allowed-value validation missing. |
| Record serial number | Partial | Parsed if numeric. No sequence reconciliation. |

## Type 3 Flight Leg

| Field/Rule | Current Status | Notes |
| --- | --- | --- |
| Record type | Done | Parsed and stored as code `3`. |
| Operational suffix | Partial | Parsed. Allowed-value validation missing. |
| Airline code | Partial | Parsed. Known carrier validation missing. |
| Flight number | Partial | Parsed. Format/range validation missing. |
| Itinerary variation identifier | Partial | Parsed. Format/range validation missing. |
| Leg sequence number | Partial | Parsed as integer. Format validation should be made explicit. |
| Service type | Partial | Parsed. Allowed-value validation missing. |
| Operating period start/end | Partial | Raw dates parsed. Date/range validation missing. |
| Operating days | Partial | Parsed. Day-mask validation missing. |
| Frequency rate | Partial | Parsed. Allowed-value validation missing. |
| Departure station | Partial | Parsed. Airport validation currently not active. |
| Departure times | Partial | Raw local/aircraft/passenger times parsed. Time validation missing. |
| Departure UTC variation | Partial | Parsed. UTC offset validation missing. |
| Departure terminal | Done | Parsed and persisted. |
| Arrival station | Partial | Parsed. Airport validation currently not active. |
| Arrival times | Partial | Raw aircraft/passenger times parsed. Time validation missing. |
| Arrival UTC variation | Partial | Parsed. UTC offset validation missing. |
| Arrival terminal | Done | Parsed and persisted. |
| Aircraft type | Partial | Parsed. Known aircraft type validation missing. |
| Booking designators/modifiers | Partial | Parsed. Cabin/booking class validation missing. |
| Meal service note | Partial | Parsed. Code validation missing. |
| Joint operation fields | Partial | Parsed. Codeshare/joint-operation semantics missing. |
| Minimum connecting time status | Partial | Parsed. Allowed-value validation missing. |
| Secure flight indicator | Partial | Parsed. Allowed-value validation missing. |
| Aircraft/cockpit/cabin owner/employer | Partial | Parsed. Carrier-code validation missing. |
| Onward flight fields | Partial | Parsed. Linkage validation missing. |
| Traffic restriction fields | Partial | Parsed. Restriction-code validation missing. |
| Aircraft configuration version | Partial | Parsed. No equipment/configuration domain validation. |
| Date variation | Partial | Parsed. Range validation missing. |
| Record serial number | Partial | Parsed. No sequence reconciliation. |
| Duplicate leg identity | Done | DB identity includes carrier, flight, suffix, itinerary variation, and leg sequence. |

## Type 4 DEI

| Field/Rule | Current Status | Notes |
| --- | --- | --- |
| Record type | Done | Parsed and stored as code `4`. |
| Flight identity fields | Partial | Parsed and used to attach DEI to a matching Type 3 leg. |
| Board/off point indicators | Partial | Parsed. Allowed-value validation missing. |
| Data element identifier | Partial | Parsed as generic identifier. Identifier-specific decoding missing. |
| Board/off points | Partial | Parsed. Airport/station validation missing. |
| DEI payload | Partial | Stored as raw 155-character payload. Manual-specific interpretation missing. |
| DEI-to-leg matching | Partial | Matches previous leg by full identity. Strict mode rejects unmatched DEIs; relaxed mode attaches to latest leg. |
| DEI-specific rules | Missing | Each supported DEI identifier needs its own parser/validator. |

## Type 5 Trailer

| Field/Rule | Current Status | Notes |
| --- | --- | --- |
| Record type | Done | Parsed and stored as code `5`. |
| Airline designator | Partial | Parsed. Cross-check with carrier missing. |
| Release date | Partial | Raw date parsed. Date validation missing. |
| Serial check reference | Partial | Parsed. No reconciliation implemented. |
| Continuation/end code | Partial | Parsed. Allowed-value and continuation behavior missing. |
| Record serial number | Partial | Parsed. No sequence reconciliation. |

## Persistence

| Area | Current Status | Notes |
| --- | --- | --- |
| Header persistence | Done | One header per logical SSIM file. |
| Carrier persistence | Done | One carrier per logical SSIM file. |
| Flight persistence | Done | Saved in chunks through `SsimFlightRepository`. |
| DEI persistence | Done | Cascaded through flight entity. For very large files, PostgreSQL COPY should replace JPA here. |
| Trailer persistence | Partial | Present on final chunk. Intermediate chunks can be trailerless. |
| Multiple airlines in one file | Done | Creates separate logical file rows per airline/carrier section. |
| UI query support | Done | API exposes file/message/flight views using existing DTOs. |

## Validation Backlog

| Priority | Item | Why |
| --- | --- | --- |
| Done | Make SSIM strict/relaxed validation mode configurable | Bulk ingestion may need relaxed mode; certification needs strict mode. |
| Partial | Reject invalid Type 2 time mode instead of storing null | Implemented in strict mode. Relaxed mode preserves previous behavior. |
| High | Validate date fields and date ranges | Many SSIM decisions depend on valid effective periods. |
| High | Validate Type 3 flight identity fields | Prevent malformed duplicate or unmatchable legs. |
| Done | Treat unmatched DEI as validation error in strict mode | Relaxed mode still supports fallback for bulk ingestion continuity. |
| High | Trailer serial/count reconciliation | Required for file integrity confidence. |
| Medium | Airport/station validation | Needed for clean operational schedules. |
| Medium | Aircraft type validation | Needed for downstream schedule quality. |
| Medium | Allowed-value validation for status, service type, restriction, indicators | Converts raw parsing into semantic SSIM validation. |
| Medium | DEI registry/parser by identifier | Required for meaningful DEI consumption. |
| Low | Convert raw SSIM dates/times to normalized date/time fields | Useful for UI/search, but raw preservation is still important. |

## Recommended Next Implementation Order

1. Implement strict failures for invalid dates, invalid day masks, invalid UTC offsets, and unsupported code values.
2. Add trailer reconciliation using record serial/check fields.
3. Add airport/aircraft/reference-data validators.
4. Build a DEI identifier registry so each supported DEI has a typed parser and validator.
5. Add PostgreSQL COPY bulk persistence for Type 3 and Type 4 rows for 10M+ record files.
