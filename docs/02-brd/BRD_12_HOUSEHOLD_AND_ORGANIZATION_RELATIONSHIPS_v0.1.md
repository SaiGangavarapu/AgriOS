# BRD 12 — Household and Organization Relationships

## 1. Objective

Represent actual agricultural participation rather than assume that the legal landowner is the only farmer.

## 2. Household model

A household may include:

- landowner
- cultivating farmer
- spouse
- adult family member
- youth participant
- worker
- record keeper
- financial decision maker

## 3. Household access

The farmer may delegate:

- view-only access
- farm-operation recording
- expense recording
- task completion
- advisory acknowledgement
- field-boundary capture
- consent management, only when explicitly permitted

## 4. Organization relationships

Supported organizations may include:

- FPO
- cooperative
- self-help group
- producer group
- village organization
- contract-farming group
- dairy society
- irrigation association
- NGO programme

## 5. Membership attributes

- organization
- membership number
- role
- start date
- end date
- status
- verification source
- services enabled
- data-sharing scope

## 6. Business rules

- A farmer may belong to multiple organizations.
- Organization membership must not automatically grant access to all farmer data.
- Household membership does not automatically grant consent-management authority.
- Delegated rights must be revocable.
- Historical membership must remain auditable.

## 7. Business requirements

- BR-026: The platform shall represent household-level farming participation.
- BR-027: The platform shall support delegated operational access.
- BR-028: The platform shall support multiple organization memberships.
- BR-029: The platform shall enforce data access separately from organization membership.
- BR-030: The platform shall retain membership history.
