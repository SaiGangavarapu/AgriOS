# API 08 — Farmer, Household, and Organization

## Farmer endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/farmers` | Register farmer |
| POST | `/farmers/assisted` | Assisted registration |
| GET | `/farmers/{farmerId}` | Get farmer |
| PATCH | `/farmers/{farmerId}` | Update profile |
| GET | `/farmers` | Search farmers |
| POST | `/farmers/{farmerId}/verify` | Add verification |
| POST | `/farmers/{farmerId}/suspend` | Suspend |
| POST | `/farmers/{farmerId}/reactivate` | Reactivate |
| GET | `/farmers/duplicate-candidates` | Review duplicates |
| POST | `/farmers/merge` | Merge confirmed duplicates |

## Household endpoints

- `POST /households`
- `GET /households/{id}`
- `POST /households/{id}/members`
- `DELETE /households/{id}/members/{memberId}`

## Organization endpoints

- `POST /organizations`
- `GET /organizations/{id}`
- `POST /organizations/{id}/memberships`
- `PATCH /organizations/{id}/memberships/{membershipId}`
- `GET /organizations/{id}/memberships`

## Registration request excerpt

```json
{
  "fullName": "Farmer Name",
  "preferredLanguage": "te",
  "mobile": "+91...",
  "location": {"villageId": "uuid"},
  "sourceChannel": "SELF"
}
```
