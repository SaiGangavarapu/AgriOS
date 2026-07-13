# SA 10 — Security Architecture

## Identity

- mobile OTP
- password/PIN where appropriate
- passkey optional
- federated identity for institutions
- MFA for privileged roles

## Authorization

- role
- tenant
- programme
- farm
- field
- action
- data category
- time
- consent

## Controls

- least privilege
- reauthentication for sensitive actions
- session listing and revocation
- rate limiting
- brute-force protection
- audit
- encryption in transit and at rest
- secret management
- secure file upload
- vulnerability scanning

## Privileged actions

Require elevated permission and audit:

- profile merge
- policy activation
- agronomy publication
- treatment approval
- actuator control
- data export
- consent override
