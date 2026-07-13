# ADR-0004: Multi-Tenant, Programme-Aware Model

## Status

Accepted

## Context

AgriOS may serve direct farmers, FPOs, NGOs, government programmes, research pilots, and commercial deployments.

## Decision

Use a tenant-aware platform model with optional programme scope. Tenant controls configuration and operational isolation. Programme represents a time-bounded intervention or service arrangement within or across a tenant where permitted.

## Consequences

- records include tenant scope
- programme scope is optional
- farmer ownership of data is not transferred to the tenant
- cross-tenant sharing requires explicit contracts and consent
- normal farmer UI hides tenancy complexity
