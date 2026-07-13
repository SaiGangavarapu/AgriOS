# BRD 63 — Identity, Authentication, and Account Protection

## 1. Objective

Protect accounts without creating unnecessary barriers for farmers and assisted users.

## 2. Authentication methods

Configurable methods may include:

- mobile OTP
- password
- PIN
- passkey
- institution-managed identity
- approved federated identity
- assisted access with controlled delegation

## 3. Account recovery

Recovery may use:

- verified mobile
- approved identity check
- institution-assisted recovery
- recovery contact
- manual support review

Recovery shall not rely only on weak personal questions.

## 4. Session management

The platform shall support:

- session expiry
- device listing
- session revocation
- suspicious-login alerts
- reauthentication for sensitive actions
- offline session constraints

## 5. Protection controls

- failed-attempt throttling
- OTP rate limits
- bot protection
- device risk
- account lock or cooldown
- audit
- secure credential storage
- notification of important account changes

## 6. Business requirements

- BR-283: Authentication methods shall be configurable by role and risk.
- BR-284: Account recovery shall require verified evidence or controlled support.
- BR-285: Sensitive actions shall support reauthentication.
- BR-286: Users shall be able to review and revoke active sessions.
- BR-287: Authentication abuse and repeated failure shall trigger protection controls.
