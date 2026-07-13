# BRD 11 — Farmer Onboarding and Identity

## 1. Objective

Enable farmers to register directly or through assisted onboarding while preserving identity accuracy, inclusiveness, privacy, and low-friction access.

## 2. Onboarding channels

- self-registration using mobile
- field-officer assisted registration
- FPO-assisted registration
- institutional import
- migration from an approved external system

## 3. Minimum farmer profile

- full name
- preferred name
- mobile number
- preferred language
- village and administrative location
- gender where voluntarily provided
- date or year of birth where needed
- farming role
- primary occupation
- accessibility needs
- communication preference

## 4. Optional profile information

- email
- alternate mobile
- household details
- identity-document reference
- bank-account reference
- government scheme identifiers
- education level
- farming experience
- livestock ownership
- equipment ownership

Optional data must not block basic onboarding unless required by a specific service.

## 5. Identity verification levels

### Level 0 — Unverified

Basic profile created without external verification.

### Level 1 — Contact verified

Mobile or other communication channel verified.

### Level 2 — Institution verified

Verified by approved field officer, FPO, cooperative, or partner.

### Level 3 — Document verified

Verified against permitted identity evidence.

The platform must display verification level and must not imply stronger verification than actually performed.

## 6. Duplicate detection

Potential duplicate checks:

- mobile number
- identity reference
- name and village
- household relationships
- organization membership

Duplicate detection must support review and merge rather than automatically discard records.

## 7. Farmer lifecycle

`Invited → Registered → Contact Verified → Profile Completed → Active`

Exceptional states:

- Suspended
- Archived
- Deceased
- Duplicate Under Review
- Merged
- Withdrawn

## 8. Business requirements

- BR-021: The platform shall support self and assisted farmer registration.
- BR-022: The platform shall support configurable identity-verification levels.
- BR-023: The platform shall support duplicate review and controlled merge.
- BR-024: The platform shall not require optional financial or identity data for basic advisory access unless legally required.
- BR-025: The platform shall record who created, verified, edited, suspended, or merged a farmer profile.
