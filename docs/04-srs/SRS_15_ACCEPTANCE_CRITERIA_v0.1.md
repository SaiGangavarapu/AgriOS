# SRS 15 — Acceptance Criteria

## AC-FARMER-001 Self-registration

Given registration is enabled
And the mobile number has no confirmed canonical farmer
When valid mandatory data is submitted
Then the system creates the farmer in Registered state
And records preferred language, tenant, source channel, and audit event.

## AC-FARMER-002 Duplicate review

Given registration data matches a configured duplicate threshold
When the registration is submitted
Then the system does not silently create a second canonical farmer
And creates or links a duplicate-review case.

## AC-FARMER-003 Assisted onboarding

Given a field officer has onboarding permission
When the officer registers a farmer
Then the system records the officer as assisting actor
And records the farmer as the data subject.

## AC-FIELD-001 Boundary validation

Given a boundary contains a self-intersection
When validation runs
Then the system marks the geometry invalid
And prevents field verification.

## AC-FIELD-002 Boundary overlap

Given a new boundary overlaps an active field above the review threshold
When validation runs
Then the system creates a review-required result
And does not automatically reject the field.

## AC-FIELD-003 Boundary versioning

Given an active field has a verified boundary
When an authorized boundary correction is approved
Then a new boundary version becomes active
And the previous version remains immutable.

## AC-TENURE-001 Expired cultivation right

Given a cultivation right has expired
When a user attempts to activate a new crop cycle
Then the system rejects the action with `TENURE_EXPIRED`.

## AC-ACCESS-001 Delegated access

Given a farmer grants operation-recording access for one field for 30 days
When the delegate records an operation within that scope and period
Then the system accepts it
And denies access to other fields.

## AC-CONSENT-001 Purpose-specific sharing

Given consent exists for insurance sharing
When a lender requests the same data
Then the system rejects the request with `CONSENT_PURPOSE_MISMATCH`.

## AC-CONSENT-002 Revocation

Given a farmer revokes active consent
When a future sharing request is made
Then the system rejects the share
And records the denied attempt.

## AC-OFFLINE-001 Draft registration

Given the device is offline
When an authorized field officer creates a valid draft farmer registration
Then the system stores it locally in Queued state
And synchronizes it later using an idempotency key.
