# SA 11 — Privacy, Consent, and Audit Architecture

## Consent service

Stores:

- purpose
- recipient
- data categories
- scope
- validity
- language
- policy version
- evidence
- revocation

## Enforcement

Consent checks occur:

- before data export
- before insurer or lender sharing
- before research use
- before buyer traceability disclosure
- before long-term voice or image reuse

## Audit

Append-only audit events include:

- actor
- action
- target
- time
- tenant
- correlation
- before/after references
- reason

## Data-subject workflows

- access
- correction
- export
- restriction
- deletion
- consent review
- complaint
