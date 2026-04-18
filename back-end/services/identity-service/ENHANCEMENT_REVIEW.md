# Auth Identity Service – Enhancement Review

This document summarizes a quick code audit and concrete enhancements to improve reliability, security, and operability.

## What I changed now

1. **Made CORS policy configurable via `security.cors.*`** rather than hardcoding `http://localhost:4200` in Java code.
2. Added support for required auth headers in CORS (`tenant-code`, `refresh-token`, `X-Refresh-Token`).
3. Added environment-variable-driven defaults in `application.yml`.

## Recommended next enhancements

### 1) Unify refresh/logout header convention
- Current API uses both `refresh-token` and `X-Refresh-Token` headers.
- Recommendation: standardize on one header (or migrate to body/cookie strategy) and keep backward compatibility for one release.

### 2) Add tenant/auth consistency check on refresh
- `refresh` validates the requested tenant exists, but does not explicitly enforce that requested `tenant-code` equals refresh-token tenant.
- Recommendation: pass `tenant-code` into refresh flow and reject mismatch.

### 3) Revisit public endpoint exposure for `/api/auth/logout`
- Logout is public and can be called without tenant context.
- Recommendation: require tenant context and authenticated session where possible.

### 4) Improve security logging behavior
- Security config logs `anyRequest().authenticated()` with message “denied by default”, which is misleading.
- Recommendation: update message to reflect authenticated requirement and reduce noisy TRACE logging in non-dev profiles.

### 5) Add integration tests for filter order and auth flows
- Add tests for:
  - missing/invalid tenant header behavior,
  - JWT filter bypass on public endpoints,
  - refresh token rotation and replay detection,
  - OIDC callback with invalid state/nonce/verifier.

### 6) Externalize and validate CORS config in Config Server profile
- Now that CORS is property-driven, define per-environment values in config-service and validate startup for empty/unsafe origins.

