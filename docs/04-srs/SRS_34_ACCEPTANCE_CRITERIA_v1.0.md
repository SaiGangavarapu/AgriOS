# SRS 34 — Acceptance Criteria

## Farmer

Given valid mandatory data and no canonical duplicate, registration creates a Registered farmer with provenance and audit.

## Field

Given invalid geometry, field verification is blocked. Given excessive overlap, review is required rather than automatic rejection.

## Soil

Given a published laboratory result, every value has parameter, method, unit, quality, and source.

## Crop plan

Given unresolved hard constraints, approval is blocked.

## Nutrient

Given no deficiency evidence, a micronutrient recommendation cannot auto-publish.

## Irrigation

Given heavy rain risk or unsafe actuator state, automated irrigation is blocked and audited.

## Advisory

Given missing confidence, evidence, target, or validity, publication is rejected.

## Crop health

Given only an image-model prediction, high-risk chemical treatment cannot be approved.

## Traceability

Given a lot split, child quantities reconcile to the parent adjustment and lineage is preserved.

## Consent

Given revoked consent, future sharing is denied and the denied attempt is audited.

## Offline

Given repeated synchronization of the same idempotency key, only one server-side business action is created.
