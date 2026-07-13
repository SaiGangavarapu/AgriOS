# Domain Service, Policy, and Specification Catalogue

## Domain services

### CropSuitabilityService

Evaluates candidate crops across multiple domain inputs.

### NutrientBudgetService

Calculates nutrient demand, credits, source allocation, and uncertainty.

### IrrigationRequirementService

Calculates irrigation requirement using crop, stage, soil, weather, and water context.

### AdvisoryAssemblyService

Combines approved decisions, evidence, confidence, and farmer-facing explanation.

### ProduceLotBalanceService

Validates quantity movements across split, merge, loss, and transfer.

### EvidencePackageService

Builds purpose-specific insurance or lending evidence packages.

## Policies

- FarmerProfileMergePolicy
- FieldOverlapReviewPolicy
- CropPlanApprovalPolicy
- NutrientPlanReviewPolicy
- MicronutrientSafetyPolicy
- WeatherLockoutPolicy
- AutomationAuthorizationPolicy
- AdvisoryPublicationPolicy
- TreatmentApprovalPolicy
- ProduceClaimPolicy
- ConsentEnforcementPolicy
- DataRetentionPolicy

## Specifications

- ActiveConsentForPurposeSpecification
- ValidSoilTestSpecification
- ValidWaterTestSpecification
- SuitableCropCandidateSpecification
- CurrentCropStageSpecification
- SafeInputApplicationSpecification
- IrrigationAllowedSpecification
- DeviceReadyForAutomationSpecification
- AdvisoryPublishableSpecification
- TraceabilityClaimSupportedSpecification

## Rule

Domain services shall be stateless and shall not become containers for unrelated application orchestration.
