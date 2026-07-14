# API 21 — Webhooks

## Inbound webhooks

- notification delivery status
- insurer status
- lender status
- buyer delivery or payment status
- laboratory result notification
- weather provider notification where offered

## Security

- HTTPS
- signature verification
- timestamp tolerance
- replay detection
- source allowlisting where appropriate
- idempotent event id

## Standard webhook envelope

```json
{
  "webhookId": "uuid",
  "eventType": "NOTIFICATION_DELIVERED",
  "occurredAt": "2026-07-14T12:00:00Z",
  "source": "provider-code",
  "payload": {}
}
```

Providers receive `2xx` only after durable acceptance, not necessarily full business processing.
