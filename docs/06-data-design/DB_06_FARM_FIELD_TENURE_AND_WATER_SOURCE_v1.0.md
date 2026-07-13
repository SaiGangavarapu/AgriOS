# DB 06 — Farm, Field, Tenure, and Water Source

## Farm tables

- `farm.farm`
- `farm.farm_participant`
- `farm.farm_infrastructure`
- `farm.farm_verification`

## Field tables

- `farm.field`
- `farm.field_boundary_version`
- `farm.management_zone`
- `farm.field_lineage`

## Water-source tables

- `farm.water_source`
- `farm.water_source_field_link`
- `farm.water_source_access`
- `farm.water_source_inspection`

## Tenure tables

- `tenure.tenure_arrangement`
- `tenure.tenure_party`
- `tenure.tenure_evidence`
- `tenure.tenure_dispute`

## Key rules

- one active field boundary version
- split and merge preserve lineage
- tenure periods cannot overlap without review state
- cultivation right and ownership are separate
