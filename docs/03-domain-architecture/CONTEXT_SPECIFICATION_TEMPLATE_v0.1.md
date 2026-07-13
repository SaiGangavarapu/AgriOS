# Bounded Context Specification Template

Each bounded context specification shall define:

## 1. Purpose

The business responsibility owned by the context.

## 2. Ubiquitous language

Terms that have one precise meaning inside the context.

## 3. Aggregate roots

Aggregate roots and their consistency boundaries.

## 4. Entities and value objects

Internal domain types.

## 5. Commands

Intent to change state.

## 6. Queries

Read use cases and projections.

## 7. Domain events

Facts emitted after successful state changes.

## 8. Invariants

Rules that must always hold.

## 9. Repositories

Persistence abstractions owned by the context.

## 10. Inbound ports

Public application interfaces.

## 11. Outbound ports

Dependencies on other contexts or external systems.

## 12. Process managers

Long-running or cross-context workflows.

## 13. Failure modes

Business rejection, technical failure, retry, and compensation.

## 14. Security and consent

Role, scope, and data-sharing constraints.

## 15. Observability

Required logs, metrics, traces, and audit events.

## 16. Extraction considerations

Conditions under which the context may become an independent service.
