# DB 08 — Agronomy Knowledge

## Tables

- `knowledge.knowledge_item`
- `knowledge.knowledge_version`
- `knowledge.evidence_reference`
- `knowledge.applicability_rule`
- `knowledge.review_record`
- `knowledge.crop`
- `knowledge.variety`
- `knowledge.growth_stage_definition`
- `knowledge.operation_template`
- `knowledge.nutrient_rule`
- `knowledge.irrigation_rule`
- `knowledge.crop_health_rule`
- `knowledge.treatment_rule`

## Rules

- published version immutable
- applicability must include geography and domain scope
- evidence grade mandatory
- commercial evidence labelled
- superseded versions retained
