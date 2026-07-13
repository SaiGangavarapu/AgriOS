# Context Specification 01 — Identity, Farmer, Household, and Consent

## Identity and Access Context

### Purpose

Authenticate actors and issue scoped authorization claims.

### Aggregate roots

- UserAccount
- AuthenticationProfile
- DelegatedAccessGrant

### Commands

- RegisterUserAccount
- VerifyContactPoint
- AddAuthenticationFactor
- RevokeSession
- GrantDelegatedAccess
- RevokeDelegatedAccess

### Queries

- GetUserAccount
- ListActiveSessions
- GetEffectivePermissions
- ListDelegatedAccess

### Invariants

- suspended account cannot authenticate
- delegated access must have scope and validity
- privileged actions may require reauthentication

## Farmer Registry Context

### Aggregate root

Farmer

### Commands

- RegisterFarmer
- UpdateFarmerProfile
- VerifyFarmer
- SuspendFarmer
- MergeFarmerProfiles

### Queries

- GetFarmer
- SearchFarmers
- GetFarmerVerification
- GetFarmerMergeHistory

### Invariants

- canonical farmer profile must be unique after merge
- verification level may only increase through approved evidence or decrease through review
- merged profiles become read-only aliases

## Household and Organization Context

### Aggregate roots

- Household
- Organization
- OrganizationMembership

### Commands

- CreateHousehold
- AddHouseholdMember
- RemoveHouseholdMember
- RegisterOrganization
- AddOrganizationMember
- EndOrganizationMembership

### Invariants

- membership does not imply unrestricted data access
- household membership does not imply consent authority

## Consent Context

### Aggregate root

ConsentGrant

### Commands

- GrantConsent
- RevokeConsent
- RenewConsent
- ExpireConsent

### Queries

- GetActiveConsent
- ListConsentHistory
- CheckPurposeAuthorization

### Invariants

- active consent requires purpose, recipient, scope, and validity
- revoked consent cannot authorize future sharing
- broader sharing cannot be inferred from narrower consent
