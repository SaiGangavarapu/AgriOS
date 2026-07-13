# DB 09 — Crop Planning, Crop Cycle, Seed, Operations, and Tasks

## Crop planning

- `cropplanning.crop_plan`
- `cropplanning.crop_plan_version`
- `cropplanning.crop_candidate`
- `cropplanning.crop_plan_scenario`
- `cropplanning.resource_requirement`
- `cropplanning.risk_assessment`

## Crop cycle

- `cropcycle.crop_cycle`
- `cropcycle.field_allocation`
- `cropcycle.stage_observation`
- `cropcycle.crop_loss`
- `cropcycle.season_closure`

## Seed

- `seed.seed_lot`
- `seed.germination_test`
- `seed.seed_treatment`
- `seed.seed_allocation`

## Operations and tasks

- `operations.farm_operation`
- `operations.input_application`
- `operations.labour_usage`
- `operations.machinery_usage`
- `tasks.task`
- `tasks.task_dependency`
- `tasks.task_assignment`
- `tasks.task_completion`

## Integrity

- approved crop plan version immutable
- field allocation cannot exceed field area silently
- seed allocation cannot exceed lot availability
- closed crop cycle rejects new operations
