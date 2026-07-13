# ADR-0003: Separate Farmer and User Identities

## Status

Accepted

## Context

A farmer may be registered before having a login. A household operator, field officer, or FPO representative may act for a farmer. Treating Farmer and UserAccount as the same entity would exclude assisted workflows and create incorrect identity assumptions.

## Decision

Farmer is a domain identity owned by Farmer Registry. UserAccount is an authentication identity owned by Identity and Access. A farmer may have zero, one, or more authorized user-account links over time.

## Consequences

- assisted onboarding is supported
- household delegation is explicit
- account recovery does not mutate farmer identity
- authorization must resolve actor-to-farmer relationships
