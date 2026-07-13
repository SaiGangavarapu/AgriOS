# Context Specification 06 — Advisory, Expert Services, and Crop Health

## Advisory Context

### Aggregate root

Advisory

### Commands

- GenerateAdvisory
- RequestAdvisoryReview
- ApproveAdvisory
- PublishAdvisory
- AcknowledgeAdvisory
- CompleteAdvisory
- WithdrawAdvisory
- ExpireAdvisory

### Queries

- GetActiveAdvisoriesForField
- GetAdvisoryExplanation
- GetAdvisoryEvidence
- ListUnacknowledgedAdvisories

### Invariants

- published advisory requires target, action, reason, confidence, evidence, validity, and source versions
- withdrawn advisory cannot be completed
- high-risk advisory must satisfy review policy

## Expert Services Context

### Aggregate root

ExpertCase

### Commands

- CreateExpertCase
- TriageExpertCase
- AssignExpert
- RequestAdditionalInformation
- RecordExpertDecision
- CloseExpertCase

### Invariants

- expert decision requires qualified specialty
- closure requires communicated outcome or explicit no-contact state
- expert override must state rationale

## Pest and Disease Context

### Aggregate root

CropHealthCase

### Commands

- PlanScouting
- RecordCropHealthObservation
- AddImageObservation
- AddDifferentialDiagnosis
- ConfirmDiagnosis
- DraftTreatmentPlan
- ApproveTreatmentPlan
- RecordTreatmentApplication
- ReviewTreatmentEffectiveness

### Invariants

- image inference alone cannot authorize high-risk treatment
- treatment must match crop, target, stage, and legal status
- pre-harvest and re-entry constraints must be enforced
- diagnosis correction preserves previous diagnosis
