# API 05 — Pagination, Filtering, Sorting, and Concurrency

## Pagination

Query parameters:

- `page`
- `size`
- `sort`

Maximum page size is configurable.

Response:

```json
{
  "content": [],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 125,
    "totalPages": 7
  }
}
```

Cursor pagination should be used for high-volume telemetry, audit, and event streams.

## Filtering

Standard filters:

- `status`
- `from`
- `to`
- `farmerId`
- `farmId`
- `fieldId`
- `cropCycleId`
- `programmeId`
- `updatedAfter`

## Concurrency

Version-sensitive update requests provide either:

- `If-Match: "3"`
- or a required `version` field

Stale updates return `409 RESOURCE_VERSION_CONFLICT`.
