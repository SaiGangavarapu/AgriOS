# SRS 05 — Acceptance Criteria Conventions

## Format

Acceptance criteria should use Given/When/Then where practical.

## Example

```text
Given a farmer has no active profile for the verified mobile number
When the farmer submits valid mandatory registration data
Then the system creates a farmer profile in Registered state
And records the actor, timestamp, tenant, and source channel
```

## Required coverage

Acceptance criteria must cover:

- happy path
- validation failure
- authorization failure
- conflict
- duplicate submission
- audit
- offline behavior where applicable
- localization
- accessibility where applicable

## Identifier

`AC-<domain>-NNN`

## Rule

A requirement is not ready for implementation until it has at least one acceptance criterion.
