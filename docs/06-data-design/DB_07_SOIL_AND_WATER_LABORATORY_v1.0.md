# DB 07 — Soil and Water Laboratory

## Sampling tables

- `soilwater.sample_plan`
- `soilwater.sample`
- `soilwater.sample_location`
- `soilwater.custody_event`
- `soilwater.laboratory`
- `soilwater.test_order`

## Result tables

- `soilwater.test_result`
- `soilwater.test_parameter`
- `soilwater.test_method`
- `soilwater.test_interpretation`
- `soilwater.soil_profile`
- `soilwater.water_quality_profile`
- `soilwater.detected_constraint`

## Important attributes

- sample type
- depth
- field or water source
- collected at
- collector
- laboratory method
- value
- unit
- quality state
- interpretation rule version
- valid until

## Integrity rules

- published result requires method and unit
- rejected sample cannot publish an approved result
- stale profile remains historical
