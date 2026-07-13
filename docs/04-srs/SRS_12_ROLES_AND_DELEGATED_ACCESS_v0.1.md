# SRS 12 — Roles and Delegated Access

## FR-ACCESS-001 Grant delegated access

The system shall allow an authorized farmer to grant scoped operational access.

Scope dimensions:

- farm
- field
- service
- action
- validity period

## FR-ACCESS-002 Revoke delegated access

The system shall support immediate revocation.

## FR-ACCESS-003 Temporary access

The system shall support time-bound access for visits, audits, and consultations.

## FR-ACCESS-004 Effective permissions

The system shall calculate effective permissions from role, scope, delegation, consent, and policy.

## FR-ACCESS-005 Session review

The system shall allow users to review and revoke active sessions.

## SR-ACCESS-001 Least privilege

The system shall deny actions not explicitly allowed.

## SR-ACCESS-002 Sensitive action

Consent management, profile merge, data export, and tenure-document access shall require elevated authorization.

## SR-ACCESS-003 Emergency access

Emergency access shall require reason, expiry, and post-use review.

## DR-ACCESS-001 Delegation history

The system shall retain grant, scope, expiry, revocation, and actor.
