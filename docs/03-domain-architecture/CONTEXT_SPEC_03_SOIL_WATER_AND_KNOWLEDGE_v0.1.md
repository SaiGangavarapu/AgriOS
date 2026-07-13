# Context Specification 03 — Soil, Water, and Agronomy Knowledge

## Soil and Water Context

### Aggregate roots

- SamplePlan
- SoilSample
- SoilTest
- WaterSample
- WaterTest
- SoilProfile
- WaterQualityProfile

### Commands

- PlanSoilSampling
- CollectSoilSample
- RecordCustodyTransfer
- ReceiveSoilSample
- RejectSoilSample
- PublishSoilTest
- PublishWaterTest
- InterpretSoilTest
- InterpretWaterTest

### Queries

- GetCurrentSoilProfile
- GetCurrentWaterQuality
- ListFieldTests
- GetTestValidity
- GetDetectedConstraints

### Invariants

- published test requires method, unit, source, quality status, and sample reference
- rejected sample cannot produce an approved result
- interpretation must reference a versioned rule set
- stale results remain visible but cannot appear current

## Agronomy Knowledge Context

### Aggregate root

KnowledgeItem

### Commands

- DraftKnowledgeItem
- SubmitKnowledgeForReview
- ApproveKnowledge
- PublishKnowledge
- SupersedeKnowledge
- WithdrawKnowledge

### Queries

- GetPublishedKnowledge
- SearchKnowledgeByApplicability
- GetKnowledgeVersion
- ListEvidenceReferences

### Invariants

- only approved versions may be published
- publication requires applicability, evidence grade, and review metadata
- published knowledge is immutable
- commercial evidence must be labelled as commercial

### Outbound ports

- EvidenceRepositoryPort
- DocumentReferencePort
- LocalizationPort
