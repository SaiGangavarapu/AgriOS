# API 04 — Error Model

## Error envelope

```json
{
  "timestamp": "2026-07-14T12:00:00Z",
  "status": 409,
  "code": "RESOURCE_VERSION_CONFLICT",
  "message": "The resource was changed by another request.",
  "messageKey": "error.resource.versionConflict",
  "correlationId": "uuid",
  "retryable": false,
  "fieldErrors": [
    {
      "field": "version",
      "code": "STALE_VERSION",
      "message": "Refresh and retry."
    }
  ],
  "details": {}
}
```

## HTTP usage

- `400` malformed or validation failure
- `401` unauthenticated
- `403` unauthorized or consent denied
- `404` resource not found within authorized scope
- `409` lifecycle, duplicate, idempotency, or version conflict
- `422` semantically valid request rejected by domain rules
- `429` rate limited
- `503` temporary dependency failure

## Rule

Errors must not expose secrets, internal SQL, stack traces, or another tenant's resource existence.
