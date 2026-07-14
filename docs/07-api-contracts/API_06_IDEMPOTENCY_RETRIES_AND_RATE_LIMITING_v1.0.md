# API 06 — Idempotency, Retries, and Rate Limiting

## Idempotency

Required for:

- farmer assisted registration
- offline command synchronization
- telemetry ingestion batches
- actuator commands
- evidence sharing
- notification provider callbacks
- external payment or delivery callbacks

The same key and equivalent payload return the original result. The same key with a different payload returns `409 REQUEST_IDEMPOTENCY_CONFLICT`.

## Retry guidance

Clients may retry:

- timeouts
- `429`
- retryable `503`

Clients should not automatically retry domain rejection.

## Rate limits

Separate policies for:

- public OTP
- authenticated farmer
- institutional bulk APIs
- telemetry gateway
- partner webhook
- AI assistant

Rate-limit responses include retry timing.
