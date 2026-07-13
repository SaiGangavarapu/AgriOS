# Context Specification 02 — Farm, Field, and Tenure

## Farm and Field Context

### Purpose

Maintain authoritative farm, field, boundary, zone, and infrastructure state.

### Aggregate roots

- Farm
- Field

### Commands

- RegisterFarm
- VerifyFarm
- AddFarmParticipant
- RegisterField
- CaptureFieldBoundary
- VerifyFieldBoundary
- SplitField
- MergeFields
- AddManagementZone
- UpdateFieldStatus

### Queries

- GetFarm
- ListFarmerFarms
- GetField
- GetCurrentFieldBoundary
- GetFieldHistory
- SearchFieldsByGeometry

### Invariants

- one active boundary version per field
- split and merge preserve lineage
- active management-zone area cannot exceed field area
- field version used by crop cycle must remain immutable

### Outbound ports

- GeographyLookupPort
- MapValidationPort
- AuditPort

## Tenure Context

### Aggregate root

TenureArrangement

### Commands

- RecordOwnershipDeclaration
- RecordCultivationRight
- RecordLease
- RecordSharecropping
- EndTenure
- MarkTenureDisputed

### Queries

- GetCurrentCultivationRights
- GetTenureHistory
- CheckCultivationAuthority

### Invariants

- ownership and cultivation are distinct
- overlapping active arrangements require review
- expired tenure cannot authorize a new crop cycle
- legal ownership is never inferred from operational history
