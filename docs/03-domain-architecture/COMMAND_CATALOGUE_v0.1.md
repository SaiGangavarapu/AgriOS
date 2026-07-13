# Command Catalogue

## Command design rules

Each command shall contain:

- command id
- command version
- actor
- target identifier
- issued time
- correlation id
- expected version where optimistic locking applies
- idempotency key where retries are possible
- payload
- authorization context

## Command categories

### Creation commands

Examples:

- RegisterFarmer
- RegisterFarm
- RegisterField
- RegisterDevice
- CreateCropPlan
- CreateExpertCase
- CreateProduceLot

### Lifecycle commands

Examples:

- VerifyFarmer
- ActivateCropCycle
- PublishAdvisory
- DecommissionDevice
- CloseCropCycle
- CloseSupportCase

### Correction commands

Examples:

- CorrectCropStage
- CorrectOperationRecord
- CorrectLotBalance
- CorrectFarmerProfile

Corrections shall preserve original values.

### Approval commands

Examples:

- ApproveCropPlan
- ApproveNutrientPlan
- ApproveTreatmentPlan
- ApprovePolicy
- ApproveEvidenceSharing

### External-result commands

Examples:

- RecordInsurerDecision
- RecordDeliveryResult
- RecordLaboratoryResult
- RecordPaymentStatus

## Rejection model

Command rejection shall distinguish:

- validation failure
- authorization failure
- conflict
- stale version
- policy prohibition
- missing dependency
- technical failure
