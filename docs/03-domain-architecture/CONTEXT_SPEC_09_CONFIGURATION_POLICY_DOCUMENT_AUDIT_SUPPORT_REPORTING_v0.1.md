# Context Specification 09 — Configuration, Policy, Documents, Audit, Support, and Reporting

## Configuration and Policy Context

### Aggregate roots

- ConfigurationSet
- Policy
- Programme

### Commands

- CreateConfiguration
- ApproveConfiguration
- ActivateConfiguration
- RollBackConfiguration
- CreatePolicy
- ApprovePolicy
- ActivatePolicy
- RetirePolicy
- EnrolProgrammeParticipant

### Invariants

- effective configuration is immutable
- policy applicability is explicit
- historical records retain policy version used

## Document and Media Context

### Aggregate root

StoredArtifact

### Commands

- StoreDocument
- StoreImage
- StoreVoiceRecording
- ClassifyArtifact
- UpdateRetention
- DeleteArtifact
- AnonymizeArtifact

### Invariants

- storage reference must be immutable
- artifact access follows classification and consent
- deletion may be blocked by legal retention

## Audit Context

### Aggregate root

AuditRecord

### Commands

- RecordAuditEvent
- SealAuditBatch
- ExportAuditEvidence

### Invariants

- audit records are append-only
- actor, action, target, time, and correlation are mandatory
- audit correction is another event, never an update

## Support Context

### Aggregate root

SupportCase

### Commands

- CreateSupportCase
- TriageSupportCase
- AssignSupportCase
- EscalateSupportCase
- ResolveSupportCase
- ReopenSupportCase
- CloseSupportCase

### Invariants

- closure requires resolution or accepted non-resolution reason
- safety cases cannot be auto-closed

## Reporting and Analytics Context

### Aggregate roots

- ReportDefinition
- MetricDefinition
- GovernedDataset

### Commands

- DefineMetric
- PublishReportDefinition
- BuildGovernedDataset
- ApproveResearchAccess
- RevokeResearchAccess

### Invariants

- farmer-level access requires authorization
- quality and provenance annotations are mandatory
- research access is purpose- and time-bound
