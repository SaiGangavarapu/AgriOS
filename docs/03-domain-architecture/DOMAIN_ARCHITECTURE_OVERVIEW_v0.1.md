# Domain Architecture Overview

## 1. Objective

Define stable business boundaries for AgriOS before implementation begins.

The architecture must:

- align with BRD capabilities
- isolate domain rules
- preserve data ownership
- reduce coupling
- support a modular monolith first
- permit later service extraction
- prevent AI, IoT, weather, and marketplace concerns from bypassing domain rules

## 2. Architectural style

AgriOS shall begin as a domain-aligned modular monolith using Java 21, Spring Boot 3.x, and Spring Modulith.

Each bounded context shall own:

- domain model
- business rules
- persistence model
- application services
- public API
- published events
- inbound adapters
- outbound ports

## 3. Domain categories

### Core domain

Capabilities that differentiate AgriOS:

- Farm and Field Digital Twin
- Crop Planning and Crop Cycle
- Advisory and Decision Support
- Agronomy Knowledge Governance
- Weather-Aware Farm Intelligence
- Soil, Water, Nutrient, and Irrigation Intelligence

### Supporting domains

- Pest and Disease
- IoT Device Management
- Market Intelligence
- Produce Traceability
- Expert Services
- Farm Economics
- Insurance and Lending Evidence

### Generic domains

- Identity and Access
- Notification
- Document and Media
- Audit
- Configuration
- Reporting
- Support
- Integration

## 4. Boundary rule

A bounded context may consume another context's published contract, but it shall not access another context's database tables or internal domain types.

## 5. Transaction rule

A business transaction should be strongly consistent within one aggregate and usually within one bounded context.

Cross-context workflows should use:

- domain events
- process managers
- sagas
- idempotent commands
- reconciliation

## 6. Extraction candidates

Potential future service boundaries:

- weather ingestion
- IoT telemetry ingestion
- notification delivery
- media processing
- ML inference
- external market ingestion

These are extraction candidates, not immediate microservices.
