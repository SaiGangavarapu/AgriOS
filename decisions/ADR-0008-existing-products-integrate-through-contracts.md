# ADR-0008: Existing Products Integrate Through Contracts

## Status

Accepted

## Context

Milk Register, Livestock Management, Farm Fresh, Seller ERP, and AgriOS have overlapping actors and business data but different domain models and release cycles.

## Decision

Existing products shall integrate through versioned APIs, events, and identity mappings. They shall not share mutable entities, repositories, or database tables with AgriOS.

## Consequences

- products may evolve independently
- shared identity and consent can be introduced through contracts
- duplicate data may exist as projections
- anti-corruption layers are required
- future consolidation remains possible without immediate coupling
