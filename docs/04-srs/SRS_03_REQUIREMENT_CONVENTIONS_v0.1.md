# SRS 03 — Functional Requirement Conventions

## Requirement format

Each functional requirement shall include:

- identifier
- title
- actor
- preconditions
- trigger
- system response
- postconditions
- business rules
- error conditions
- audit requirements
- acceptance criteria
- BRD traceability

## Requirement wording

Use:

> The system shall...

Avoid:

- vague language
- implementation details unless architecturally required
- untestable adjectives
- unsupported assumptions

## Priority

- Must
- Should
- Could
- Won't in current scope

## Requirement quality rules

A requirement must be:

- atomic
- testable
- unambiguous
- feasible
- traceable
- consistent
- versioned

## State-changing requirements

Must define:

- command
- validation
- authorization
- idempotency
- audit
- result state

## Query requirements

Must define:

- filters
- sorting
- pagination
- authorization
- freshness
- projection
