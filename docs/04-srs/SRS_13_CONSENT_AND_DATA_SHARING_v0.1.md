# SRS 13 — Consent and Data Sharing

## FR-CONSENT-001 Present consent

The system shall present consent in the selected language and accessible format.

## FR-CONSENT-002 Grant consent

The system shall record:

- purpose
- data categories
- recipient
- scope
- validity
- policy version
- channel
- evidence of acceptance

## FR-CONSENT-003 Revoke consent

The system shall stop future sharing when consent is revoked.

## FR-CONSENT-004 Renew consent

The system shall create a new consent version.

## FR-CONSENT-005 Check authorization

The system shall evaluate active consent before protected data sharing.

## FR-CONSENT-006 Consent history

The system shall provide consent history to authorized users.

## FR-CONSENT-007 Expiry notification

The system shall notify the farmer before configured consent expiry.

## VR-CONSENT-001 Purpose mismatch

The system shall reject sharing when purpose does not match active consent.

## VR-CONSENT-002 Recipient mismatch

The system shall reject sharing to an unapproved recipient.

## SR-CONSENT-001 Minimum necessary

Only data within the approved scope shall be shared.

## DR-CONSENT-001 Audit

Every consent grant, renewal, revocation, expiry, and data share shall be audited.
