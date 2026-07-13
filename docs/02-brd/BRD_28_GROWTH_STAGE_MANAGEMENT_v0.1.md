# BRD 28 — Growth-Stage Management

## 1. Objective

Use crop stage to determine which tasks, risks, nutrient applications, irrigation actions, and warnings are relevant.

## 2. Stage sources

Stage may be determined from:

- farmer observation
- field-officer observation
- sowing date and expected duration
- growing-degree days
- sensor or image inference
- expert confirmation

## 3. Stage confidence

The system shall record:

- stage
- observation date
- source
- confidence
- evidence
- expected next stage

## 4. Stage transitions

Transitions may be:

- manually confirmed
- automatically suggested
- expert corrected

## 5. Business rules

- stage-dependent recommendations require a current stage
- low-confidence stage may trigger confirmation
- corrected stage must preserve prior value
- stage mismatch may invalidate pending tasks

## 6. Business requirements

- BR-107: The platform shall support crop-specific growth stages.
- BR-108: Stage source and confidence shall be recorded.
- BR-109: Automatic stage suggestions shall require confirmability.
- BR-110: Stage correction shall be auditable.
- BR-111: Pending tasks shall be re-evaluated after stage change.
