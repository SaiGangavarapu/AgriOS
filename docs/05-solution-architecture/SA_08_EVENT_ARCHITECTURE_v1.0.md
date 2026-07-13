# SA 08 — Event Architecture

## Initial mode

Use Spring Modulith application events with transactional outbox.

## Externalization trigger

Move selected events to Kafka/Redpanda when:

- independent scaling is required
- replay is operationally necessary
- external consumers are added
- ingestion volume demands decoupling

## Required envelope

- event id
- event type
- version
- occurred time
- producer
- aggregate id and version
- tenant
- actor
- correlation
- causation
- payload

## Reliability

- transactional outbox
- idempotent inbox
- retry with backoff
- dead-letter handling
- schema compatibility
- replay-safe consumers
