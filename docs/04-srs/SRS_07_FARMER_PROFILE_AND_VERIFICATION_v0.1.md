# SRS 07 — Farmer Profile and Verification

## FR-FARMER-006 View profile

The system shall allow an authorized actor to view a farmer profile within permitted scope.

## FR-FARMER-007 Update profile

The system shall allow permitted profile attributes to be updated with optimistic concurrency.

## FR-FARMER-008 Verification

The system shall support configurable verification levels:

- Unverified
- Contact Verified
- Institution Verified
- Document Verified

## FR-FARMER-009 Verification evidence

The system shall record evidence type, verifier, date, source, and expiry where applicable.

## FR-FARMER-010 Duplicate review

The system shall provide a review queue for potential duplicate profiles.

## FR-FARMER-011 Merge

The system shall merge duplicate farmer profiles only through an authorized workflow.

Postconditions:

- canonical farmer selected
- aliases retained
- dependent references mapped
- merged profiles made read-only
- audit event recorded

## FR-FARMER-012 Suspend and reactivate

The system shall support suspension and reactivation with reason and authorization.

## VR-FARMER-003 Stale update

The system shall reject profile updates when the expected version is stale.

## SR-FARMER-002 Sensitive attributes

Sensitive identity evidence shall be restricted to authorized roles.

## DR-FARMER-002 History

The system shall retain profile correction and merge history.
