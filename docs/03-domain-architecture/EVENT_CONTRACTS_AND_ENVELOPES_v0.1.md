# Event Contracts and Envelopes

## Standard event envelope

Every published event shall include:

```json
{
  "eventId": "uuid",
  "eventType": "CropPlanApproved",
  "eventVersion": 1,
  "occurredAt": "RFC3339 timestamp",
  "publishedAt": "RFC3339 timestamp",
  "producer": "cropplanning",
  "aggregateType": "CropPlan",
  "aggregateId": "uuid",
  "aggregateVersion": 7,
  "correlationId": "uuid",
  "causationId": "uuid",
  "tenantId": "uuid",
  "programmeId": "uuid-or-null",
  "actor": {
    "actorId": "uuid",
    "actorType": "USER|SYSTEM|DEVICE|EXTERNAL"
  },
  "payload": {}
}
```

## Event design rules

- Events are facts, not commands.
- Event names use past tense.
- Payload contains business meaning, not persistence entities.
- Sensitive fields are minimized.
- Canonical identifiers and units are used.
- External provider payloads are never embedded as canonical event payloads.
- Consumers must tolerate additive optional fields.

## Example: CropPlanApproved

Payload:

- cropPlanId
- fieldId
- fieldVersion
- cropId
- varietyId
- season
- plannedArea
- farmingSystem
- approvedBy
- approvedAt
- planVersion

## Example: SoilTestPublished

Payload:

- soilTestId
- sampleId
- fieldId
- laboratoryId
- testedAt
- publishedAt
- interpretationVersion
- qualityStatus
- constraintCodes

Detailed analytical values may be retrieved through the owning context when not appropriate for the event.

## Example: WeatherForecastUpdated

Payload:

- weatherProductId
- providerId
- issueTime
- validFrom
- validTo
- geometryReference
- variableSummary
- confidence
- sourceVersion

## Example: AdvisoryPublished

Payload:

- advisoryId
- targetType
- targetId
- category
- priority
- validFrom
- validTo
- confidence
- reviewStatus
- messageKey

## Example: ActuatorCommandBlocked

Payload:

- commandId
- deviceId
- actuatorId
- requestedAction
- blockedAt
- blockingRuleCodes
- safetyRuleVersion

## Compatibility

- Version 1 remains immutable.
- Breaking payload changes create a new event version.
- Consumers should not infer missing fields.
- Schema registry or equivalent validation should be introduced when events leave the process boundary.
