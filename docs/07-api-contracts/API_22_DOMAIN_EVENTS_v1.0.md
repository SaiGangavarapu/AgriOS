# API 22 — Domain Events

## Standard envelope

- event id
- event type
- event version
- occurred at
- published at
- producer
- aggregate type and id
- aggregate version
- tenant and programme
- actor
- correlation and causation
- payload

## Event groups

- FarmerRegistered, FarmerProfilesMerged
- FieldBoundaryVerified, FieldSplit, FieldsMerged
- SoilTestPublished, WaterConstraintDetected
- CropPlanApproved, CropCycleActivated, CropStageChanged
- NutrientPlanApproved, IrrigationScheduled
- OfficialWeatherWarningReceived, ClimateRiskDetected
- DeviceCommissioned, TelemetryValidated, ActuatorCommandBlocked
- AdvisoryPublished, ExpertDecisionIssued
- DiagnosisConfirmed, TreatmentApplied
- HarvestRecorded, ProduceLotCreated, ProduceLotSplit
- BuyerOfferAccepted, PaymentRecorded
- InsuranceEvidencePrepared, LendingEvidenceShared

Detailed payloads must minimize sensitive data and use canonical identifiers.
