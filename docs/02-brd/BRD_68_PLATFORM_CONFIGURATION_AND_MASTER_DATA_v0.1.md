# BRD 68 — Platform Configuration and Master Data

## 1. Objective

Allow controlled administration of reference data, pilot scope, feature availability, geography, and business rules.

## 2. Master-data domains

- countries, states, districts, blocks, villages
- units and conversions
- languages
- crops
- varieties
- growth stages
- soil properties
- water properties
- input categories
- operation types
- device types
- advisory categories
- risk levels
- organization types
- tenure types
- consent purposes

## 3. Configuration scope

Configuration may apply to:

- global platform
- country
- state
- district
- programme
- organization
- tenant
- pilot
- user role

## 4. Feature control

Support:

- enabled modules
- supported crops
- supported languages
- allowed integrations
- offline limits
- notification channels
- device capabilities
- automation modes
- experimental features

## 5. Change control

Master-data changes shall support:

- draft
- review
- approval
- effective date
- rollback
- impact assessment
- audit

## 6. Business requirements

- BR-308: The platform shall support scoped master-data configuration.
- BR-309: Feature availability shall be configurable by pilot, tenant, geography, and role.
- BR-310: Reference-data changes shall support approval and effective dates.
- BR-311: Unit conversions shall be centrally governed.
- BR-312: Historical records shall retain original reference-data meaning.
