# DB 12 — Advisory, Expert, and Crop Health

## Advisory

- `advisory.advisory`
- `advisory.advisory_version`
- `advisory.advisory_evidence`
- `advisory.advisory_explanation`
- `advisory.advisory_delivery`
- `advisory.advisory_response`

## Expert

- `expert.expert_profile`
- `expert.expert_specialty`
- `expert.expert_case`
- `expert.expert_case_evidence`
- `expert.expert_decision`

## Crop health

- `crophealth.scouting_plan`
- `crophealth.crop_health_observation`
- `crophealth.image_observation`
- `crophealth.differential_diagnosis`
- `crophealth.treatment_plan`
- `crophealth.treatment_application`
- `crophealth.effectiveness_review`

## Integrity

- published advisory requires evidence, confidence, target, and validity
- expert decision references reviewer specialty
- high-risk treatment cannot be approved from image inference alone
