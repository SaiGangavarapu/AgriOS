# Context Specification 04 — Crop Planning, Crop Cycle, Seed, Operations, and Tasks

## Crop Planning Context

### Aggregate root

CropPlan

### Commands

- AssessCropSuitability
- GenerateCropPlan
- SelectCropPlanScenario
- ApproveCropPlan
- ReviseCropPlan
- RejectCropPlan

### Queries

- GetCropPlan
- CompareCropPlanScenarios
- ListCandidateCrops
- GetPlanRisks
- GetPlanResourceRequirements

### Invariants

- approved plan references immutable field, knowledge, and evidence versions
- farmer override requires rationale
- hard-constraint violation cannot be hidden by score

## Crop Cycle Context

### Aggregate root

CropCycle

### Commands

- ActivateCropCycle
- RecordSowing
- RecordEstablishment
- ChangeCropStage
- CorrectCropStage
- RecordCropLoss
- CloseCropCycle

### Invariants

- crop cycle requires approved plan or documented exception
- sowing cannot precede activation
- closed cycle cannot accept new operations
- stage correction preserves history

## Seed Context

### Aggregate roots

- SeedLot
- PlantingMaterialLot

### Commands

- RegisterSeedLot
- RecordGerminationTest
- RecordSeedTreatment
- AllocateSeedToCropCycle
- ReleaseSeedAllocation

### Invariants

- allocation cannot exceed available quantity
- expired seed requires warning or block according to policy
- treatment must be compatible with lot and crop cycle

## Farm Operations Context

### Aggregate root

FarmOperation

### Commands

- PlanFarmOperation
- StartFarmOperation
- CompleteFarmOperation
- FailFarmOperation
- RecordInputApplication
- CorrectOperationRecord

### Invariants

- operation must reference current crop cycle or field
- input application requires quantity, unit, actor, and product
- corrections preserve original facts

## Task and Calendar Context

### Aggregate root

Task

### Commands

- CreateTask
- AssignTask
- StartTask
- CompleteTask
- DeferTask
- CancelTask
- SupersedeTask

### Invariants

- completed task cannot be cancelled without correction workflow
- dependent task cannot become actionable before prerequisite unless explicitly overridden
- superseded task remains auditable
