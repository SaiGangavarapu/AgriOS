# SRS 10 — Field and Boundary Management

## FR-FIELD-001 Register field

The system shall create a field under an active farm.

## FR-FIELD-002 Capture boundary

The system shall support:

- GPS walk
- map drawing
- approved file import
- assisted capture

## FR-FIELD-003 Store geometry metadata

The system shall store:

- capture method
- coordinate reference
- device
- actor
- date
- accuracy estimate
- area
- geometry validation status

## FR-FIELD-004 Validate geometry

The system shall detect:

- self-intersection
- invalid closure
- implausible area
- excessive overlap
- invalid location
- pilot-boundary violation

## FR-FIELD-005 Verify field

The system shall support field verification.

## FR-FIELD-006 Split field

The system shall create successor fields and preserve lineage.

## FR-FIELD-007 Merge fields

The system shall merge eligible fields and preserve predecessor lineage.

## FR-FIELD-008 Management zones

The system shall support zones within a field.

## FR-FIELD-009 Boundary versioning

Every approved boundary change shall create a new immutable boundary version.

## VR-FIELD-001 Active boundary

An active field shall have exactly one active boundary version.

## VR-FIELD-002 Allocation area

Zone area shall not exceed current field area without review.

## SR-FIELD-001 Geometry visibility

Exact field geometry shall be restricted by role and consent.

## DR-FIELD-001 Version reference

Crop cycles shall reference the field-boundary version used at activation.
