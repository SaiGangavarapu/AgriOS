# Domain Event Catalogue

## Farmer and access

- FarmerRegistered
- FarmerContactVerified
- FarmerVerificationChanged
- FarmerSuspended
- FarmerProfilesMerged
- DelegatedAccessGranted
- DelegatedAccessRevoked
- ConsentGranted
- ConsentRevoked
- ConsentExpired

## Farm and field

- FarmRegistered
- FarmVerified
- FieldRegistered
- FieldBoundaryCaptured
- FieldBoundaryVerified
- FieldSplit
- FieldsMerged
- WaterSourceRegistered
- TenureRecorded
- TenureExpired

## Soil and water

- SoilSamplingPlanned
- SoilSampleCollected
- SoilSampleReceived
- SoilSampleRejected
- SoilTestPublished
- SoilConstraintDetected
- WaterTestPublished
- WaterConstraintDetected

## Knowledge

- KnowledgeDrafted
- KnowledgeReviewed
- KnowledgePublished
- KnowledgeSuperseded
- KnowledgeWithdrawn

## Planning and crop cycle

- CropSuitabilityAssessed
- CropPlanGenerated
- CropPlanApproved
- CropPlanRevised
- CropCycleActivated
- SowingRecorded
- CropStageChanged
- CropStageCorrected
- CropLossRecorded
- CropCycleClosed

## Seed and operations

- SeedLotRegistered
- SeedAllocatedToCropCycle
- SeedTreatmentRecorded
- FarmOperationPlanned
- FarmOperationCompleted
- InputApplicationRecorded
- OperationFailed

## Nutrient and irrigation

- NutrientPlanGenerated
- NutrientPlanApproved
- NutrientPlanRevised
- NutrientApplicationDue
- IrrigationPlanGenerated
- IrrigationScheduled
- IrrigationSkipped
- IrrigationCompleted

## Weather and IoT

- WeatherForecastUpdated
- OfficialWeatherWarningReceived
- ClimateRiskDetected
- DeviceRegistered
- DeviceInstalled
- DeviceCommissioned
- DeviceAssigned
- DeviceHealthChanged
- CalibrationExpired
- TelemetryValidated
- TelemetryRejected
- TelemetryGapDetected
- ActuatorCommandRequested
- ActuatorCommandBlocked
- ActuatorCommandCompleted

## Advisory and expert

- AdvisoryGenerated
- AdvisoryReviewRequested
- AdvisoryApproved
- AdvisoryPublished
- AdvisoryAcknowledged
- AdvisoryCompleted
- AdvisoryExpired
- AdvisoryWithdrawn
- ExpertCaseCreated
- ExpertCaseAssigned
- ExpertDecisionIssued

## Crop health

- ScoutingCompleted
- CropHealthIssueSuspected
- DiagnosisConfirmed
- TreatmentPlanApproved
- TreatmentApplied
- TreatmentEffectivenessReviewed

## Harvest, market, and economics

- HarvestPlanned
- HarvestRecorded
- ProduceLotCreated
- ProduceLotSplit
- ProduceLotsMerged
- ProduceGradeChanged
- BuyerOfferReceived
- BuyerOfferAccepted
- ProduceDelivered
- PaymentRecorded
- CropCycleEconomicsUpdated

## Insurance and lending

- LossEventRecorded
- InsuranceEvidencePrepared
- InsuranceClaimSubmitted
- LendingEvidencePrepared
- LendingEvidenceShared
