# ADR-0006: Advisory Context Does Not Own Domain Rules

## Status

Accepted

## Context

Advisories depend on nutrient, irrigation, weather, crop-health, and crop-planning rules. Centralizing all rules in Advisory would create a large, unstable context.

## Decision

Source domains own calculations, safety rules, and decisions. Advisory owns assembly, explanation, review state, publication, delivery linkage, acknowledgement, expiry, and supersession.

## Consequences

- agronomy rules stay domain-specific
- advisory can combine multiple approved outputs
- LLM usage remains explanation-focused
- domain decisions remain testable without notification or UI concerns
