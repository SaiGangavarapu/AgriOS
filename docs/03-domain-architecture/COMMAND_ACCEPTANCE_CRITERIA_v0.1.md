# Command Acceptance Criteria

## General command acceptance

Every state-changing command must pass:

1. schema validation
2. actor authentication
3. authorization
4. consent check where applicable
5. target existence
6. expected aggregate version check
7. lifecycle transition validation
8. invariant validation
9. policy-version resolution
10. idempotency check
11. audit-context creation

## RegisterFarmer

Accept when:

- mandatory profile data is present
- mobile or contact format is valid
- no confirmed canonical duplicate exists
- actor has onboarding permission

Reject when:

- identity is prohibited or invalid
- duplicate has already been resolved
- required consent presentation cannot be established for the channel

## CaptureFieldBoundary

Accept when:

- farm exists
- actor has field-management authority
- geometry is valid enough for draft capture
- coordinate reference is known

Route for review when:

- overlap exceeds threshold
- area is implausible
- location falls outside pilot geography
- boundary conflicts with an active field

## PublishSoilTest

Accept when:

- sample is received and not rejected
- test results have parameter, value, unit, and method
- laboratory and quality status are known
- reviewer authority is valid

Reject when:

- sample identity is unresolved
- required method metadata is absent
- values use unsupported units without conversion

## ApproveCropPlan

Accept when:

- field and field version are active
- current cultivation authority exists
- hard constraints are resolved or explicitly prohibited
- farmer selection is recorded
- required expert review is complete

## ActivateCropCycle

Accept when:

- crop plan is approved
- field allocation is valid
- active tenure covers expected period
- overlapping crop-cycle rules are satisfied
- required seed or exception is recorded

## ApproveNutrientPlan

Accept when:

- current soil context is available or missing-data policy permits
- nutrient budget is internally balanced
- product conversions are traceable
- micronutrient evidence exists where required
- review policy is satisfied

## PublishAdvisory

Accept when:

- target is valid
- action, reason, confidence, evidence, validity, and source versions are present
- high-risk review is complete
- no active contradictory advisory remains unresolved
- translation fallback is available

## RequestActuatorCommand

Accept when:

- device is active and assigned
- actuator authorization is active
- telemetry freshness requirement is met
- calibration is valid
- safety interlocks pass
- command has expiry and idempotency key

## ApproveTreatmentPlan

Accept when:

- diagnosis status is sufficient
- crop and target are supported
- legal product status is current
- stage, dose, weather, interval, and harvest constraints pass
- required expert approval is present

## SplitProduceLot

Accept when:

- parent lot is active
- requested child quantities are positive
- total child quantity does not exceed available quantity
- lineage metadata is complete
- ownership and storage constraints pass

## ShareEvidencePackage

Accept when:

- farmer has reviewed the scope
- active purpose-specific consent exists
- recipient and validity are explicit
- requested data is within minimum necessary scope
- package has verification and provenance metadata
