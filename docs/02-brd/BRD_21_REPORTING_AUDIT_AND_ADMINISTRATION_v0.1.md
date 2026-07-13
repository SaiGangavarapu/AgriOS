# BRD 21 — Reporting, Audit, and Administration

## 1. Farmer reports

- farmer profile
- farm and field summary
- active crop cycles
- water-source summary
- consent summary
- delegated-access summary
- field-visit history

## 2. Field-officer dashboards

- assigned farmers
- overdue visits
- incomplete onboarding
- unverified fields
- pending follow-up
- synchronization failures
- consent issues

## 3. Administrator dashboards

- registrations by geography
- verification status
- duplicate review queue
- field-boundary issues
- consent status
- access anomalies
- organization membership
- audit events

## 4. Audit events

At minimum:

- create
- update
- verify
- approve
- suspend
- merge
- split
- consent grant
- consent revoke
- access delegation
- data export
- data share
- sensitive document access

## 5. Administration

Configurable master data:

- geography
- farm types
- tenure types
- water-source types
- roles
- verification methods
- consent purposes
- lifecycle states
- validation thresholds

## 6. Business requirements

- BR-071: The platform shall provide farmer, field-officer, and administrator reporting.
- BR-072: Sensitive actions shall generate immutable audit events.
- BR-073: Administrative reference data shall be configurable.
- BR-074: Reports shall respect role and consent boundaries.
- BR-075: Data exports shall record purpose, actor, scope, and timestamp.
