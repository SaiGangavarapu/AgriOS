# Context Specification 08 — Insurance, Lending, Notification, and Localization

## Insurance Evidence Context

### Aggregate root

InsuranceCase

### Commands

- RegisterPolicyReference
- RecordLossEvent
- PrepareInsuranceEvidence
- ApproveInsuranceEvidence
- SubmitInsuranceClaimReference
- RecordInsurerDecision

### Invariants

- farmer approval required before evidence submission
- self-declared and verified evidence remain distinct
- context does not decide claim approval

## Lending Evidence Context

### Aggregate root

LendingEvidenceRequest

### Commands

- CreateLendingEvidenceRequest
- SelectEvidenceScope
- ApproveEvidenceSharing
- ShareEvidencePackage
- RevokeLenderAccess
- ExpireLenderAccess

### Invariants

- no evidence sharing without active purpose-specific consent
- context does not make credit decisions
- access expires or is revocable

## Notification Context

### Aggregate root

Notification

### Commands

- ScheduleNotification
- SendNotification
- RecordDeliveryResult
- TriggerFallbackChannel
- AcknowledgeNotification
- ExpireNotification

### Invariants

- critical notification has expiry and source event
- duplicate delivery attempt must be idempotent
- non-critical communication respects preferences

## Localization Context

### Aggregate root

LanguagePack

### Commands

- CreateTranslationKey
- DraftTranslation
- ReviewTranslation
- PublishTranslation
- SupersedeTranslation

### Invariants

- critical safety and legal text require human review
- canonical source key remains stable
- missing translation must have defined fallback behavior
