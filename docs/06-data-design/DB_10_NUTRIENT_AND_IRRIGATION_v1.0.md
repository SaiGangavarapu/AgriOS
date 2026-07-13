# DB 10 — Nutrient and Irrigation

## Nutrient tables

- `nutrient.nutrient_plan`
- `nutrient.nutrient_plan_version`
- `nutrient.nutrient_requirement`
- `nutrient.nutrient_credit`
- `nutrient.source_allocation`
- `nutrient.product_conversion`
- `nutrient.application_schedule`

## Irrigation tables

- `irrigation.irrigation_plan`
- `irrigation.irrigation_schedule`
- `irrigation.irrigation_execution`
- `irrigation.water_accounting_entry`
- `irrigation.irrigation_safety_check`

## Integrity

- product conversion traces to nutrient requirement
- micronutrient recommendation references evidence
- measured and estimated irrigation values are distinct
- automated execution references passed safety checks
