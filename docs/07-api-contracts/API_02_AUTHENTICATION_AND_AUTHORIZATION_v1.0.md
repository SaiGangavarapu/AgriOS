# API 02 — Authentication and Authorization

## Authentication endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/auth/otp/request` | Request mobile OTP |
| POST | `/auth/otp/verify` | Verify OTP and create session |
| POST | `/auth/token/refresh` | Refresh access token |
| POST | `/auth/logout` | Revoke current session |
| GET | `/auth/sessions` | List active sessions |
| DELETE | `/auth/sessions/{sessionId}` | Revoke a session |
| POST | `/auth/reauthenticate` | Reauthenticate for sensitive action |

## Authorization dimensions

- tenant
- programme
- role
- organization
- farmer
- farm
- field
- service
- action
- data category
- validity period
- consent

## Token claims

Minimum claims:

- subject/user id
- tenant id
- roles
- permission version
- authentication strength
- session id
- issued and expiry times

Fine-grained field or farm access should normally be resolved server-side rather than embedded exhaustively in tokens.

## Sensitive actions

Require reauthentication or stronger permission:

- profile merge
- consent grant for financial sharing
- exact geometry export
- agronomy publication
- treatment approval
- actuator command
- bulk export
- policy activation
