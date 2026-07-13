# Aggregate and Entity Catalogue

## Farmer Registry

### Aggregate: Farmer

Entities:

- VerificationRecord
- FarmerContact

Value objects:

- FarmerId
- VerificationLevel
- PreferredLanguage
- ContactPoint

Invariants:

- one active canonical profile per verified identity
- merge preserves lineage
- suspended farmer cannot create new consent grants

## Farm and Field

### Aggregate: Farm

Entities:

- FarmParticipant
- FarmInfrastructureReference

### Aggregate: Field

Entities:

- FieldBoundaryVersion
- ManagementZone

Value objects:

- FieldId
- Area
- GeoPolygon
- FieldStatus

Invariants:

- active field has one current boundary version
- split and merge preserve lineage
- allocation cannot silently exceed field area

## Soil and Water

### Aggregate: SoilSample

Entities:

- CustodyEvent

### Aggregate: SoilTest

Entities:

- SoilTestResult

### Aggregate: WaterTest

Entities:

- WaterTestResult

Invariants:

- published result requires method, unit, source, and quality status
- rejected sample cannot publish results

## Agronomy Knowledge

### Aggregate: KnowledgeItem

Entities:

- EvidenceReference
- ApplicabilityRule
- ReviewRecord

Invariants:

- only approved versions may be published
- superseded versions remain immutable

## Crop Planning

### Aggregate: CropPlan

Entities:

- CropPlanScenario
- CandidateOption
- ResourceRequirement
- RiskAssessment

Invariants:

- approved plan has explicit field version
- selected crop is among evaluated candidates or has documented override

## Crop Cycle

### Aggregate: CropCycle

Entities:

- FieldAllocation
- StageObservation
- CropLossRecord
- SeasonClosure

Invariants:

- sowing cannot precede activation
- closure requires outcome status
- stage corrections preserve history

## Nutrient Management

### Aggregate: NutrientPlan

Entities:

- NutrientRequirement
- NutrientSourceAllocation
- ApplicationSchedule

Invariants:

- product quantity must trace to nutrient calculation
- low-confidence plan requires review

## Irrigation

### Aggregate: IrrigationPlan

Entities:

- IrrigationSchedule
- IrrigationExecution
- WaterAccountingEntry

Invariants:

- execution must reference source and field
- automated execution requires passed safeguards

## IoT Device

### Aggregate: Device

Entities:

- Sensor
- Actuator
- DeviceAssignment
- CalibrationRecord
- MaintenanceRecord

Invariants:

- active device has valid assignment
- actuator command prohibited when critical health state exists

## Advisory

### Aggregate: Advisory

Entities:

- AdvisoryEvidence
- AdvisoryExplanation
- AdvisoryDelivery
- FarmerResponse

Invariants:

- published advisory has confidence, evidence, validity, and target
- withdrawn advisory cannot be completed

## Pest and Disease

### Aggregate: CropHealthCase

Entities:

- Observation
- DifferentialDiagnosis
- TreatmentPlan
- EffectivenessReview

Invariants:

- high-risk treatment requires required review
- image inference alone cannot confirm high-risk treatment

## Produce Traceability

### Aggregate: ProduceLot

Entities:

- LotTransformation
- LotQualityRecord
- LotOwnershipRecord

Invariants:

- quantity balance cannot become negative
- split child quantities must reconcile with parent adjustment

## Consent

### Aggregate: ConsentGrant

Entities:

- ConsentScope
- ConsentEvidence

Invariants:

- active consent has purpose, recipient, scope, and validity
- revoked consent cannot authorize future sharing
