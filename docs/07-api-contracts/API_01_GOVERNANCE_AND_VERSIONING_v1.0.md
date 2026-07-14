# API 01 — Governance and Versioning

## Base path

`/api/v1`

## Principles

- APIs expose application use cases, not persistence entities.
- Request and response DTOs are immutable contracts.
- Domain identifiers are UUID strings.
- All timestamps use RFC 3339 UTC.
- Units use canonical codes from governed master data.
- Breaking changes require a new major API version.
- Additive optional fields are permitted within a major version.
- Deprecated endpoints remain documented with a removal date.
- Exact database schema and internal module types are never exposed.

## Required headers

- `Authorization: Bearer <token>`
- `X-Correlation-Id`
- `Idempotency-Key` for retryable state-changing operations
- `If-Match` for version-sensitive updates where applicable
- `Accept-Language`
- `X-Tenant-Id` only for trusted institutional clients when tenant is not derived from identity

## Content types

- `application/json`
- `multipart/form-data` for controlled uploads
- `application/geo+json` for supported geometry exchange
- `text/csv` only for approved imports or exports

## Deprecation

Responses may include:

- `Deprecation`
- `Sunset`
- `Link` to replacement documentation
