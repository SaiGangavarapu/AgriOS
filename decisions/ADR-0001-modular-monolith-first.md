# ADR-0001: Start with a Modular Monolith

## Status

Accepted

## Context

AgriOS has many business domains, but the initial team and deployment environment do not justify immediate microservice complexity.

## Decision

The first implementation will use Java 21, Spring Boot 3.x, and Spring Modulith. Modules will have explicit boundaries, events, and separate package ownership.

## Consequences

### Positive

- simpler deployment
- easier local development
- lower infrastructure cost
- strong transactional consistency
- easier refactoring while the domain model is still evolving

### Negative

- module boundaries require discipline
- some high-volume workloads may later need extraction

## Future extraction candidates

- IoT ingestion
- weather ingestion
- satellite processing
- notification delivery
- machine-learning inference
