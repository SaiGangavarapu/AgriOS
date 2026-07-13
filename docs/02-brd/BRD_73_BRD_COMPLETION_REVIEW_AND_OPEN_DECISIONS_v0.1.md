# BRD 73 — BRD Completion Review and Open Decisions

## 1. Purpose

Summarize the BRD baseline completed through Patch 008 and identify decisions that must be resolved before domain architecture and detailed SRS work are finalized.

## 2. Covered BRD domains

- business context and stakeholders
- farmer, household, and organization
- farm, field, tenure, and water sources
- soil, water, crop, variety, and seed
- crop planning, cycles, stages, operations, and tasks
- nutrient and irrigation
- weather, monsoon, IoT, and automation
- advisory and expert services
- pest, disease, treatment, harvest, and storage
- markets, economics, insurance, lending, and traceability
- multilingual, voice, offline, notifications
- identity, authorization, privacy, security
- configuration, analytics, support, continuity, and interoperability

## 3. Open product decisions

- first pilot state and districts
- first supported crops
- initial languages
- initial user groups
- farmer direct onboarding versus institution-led onboarding
- integration with existing livestock, dairy, marketplace, and ERP products
- payment and commercial transaction boundary
- insurance and lender pilot scope
- certification support scope
- public versus private knowledge content

## 4. Open architecture decisions

- single tenant versus multi-tenant operating model
- shared identity across existing products
- modular monolith module boundaries
- event platform timing
- IoT gateway protocol
- telemetry-store choice
- GIS and map provider
- weather-provider access
- notification providers
- object-storage provider
- search and vector-store strategy
- local AI versus hosted AI

## 5. Open governance decisions

- agronomy review board
- legal jurisdiction and privacy review
- approved evidence institutions
- commercial conflict-of-interest policy
- farmer data-sharing agreement
- research ethics workflow
- model risk governance
- incident ownership

## 6. Entry criteria for domain architecture

Domain architecture can proceed when:

- BRD baseline is accepted as the working scope
- initial pilot assumptions are recorded
- major product boundaries are explicit
- shared platform integrations are identified
- unresolved items are captured as decisions rather than silently assumed

## 7. Business requirements

- BR-333: The platform programme shall maintain an explicit open-decision register.
- BR-334: Pilot assumptions shall be approved before implementation scope is fixed.
- BR-335: Product boundaries with livestock, dairy, marketplace, and ERP shall be documented.
- BR-336: Agronomy, privacy, security, and commercial governance ownership shall be assigned.
- BR-337: Domain architecture shall trace bounded contexts to BRD capabilities.
- BR-338: SRS requirements shall trace to BRD identifiers.
- BR-339: Implementation shall not silently resolve unresolved policy decisions.
- BR-340: Material scope changes shall follow documented change control.
