# SA 15 — Offline-First Architecture

## Client storage

Use IndexedDB for PWA or SQLite for native mobile.

Cache:

- farmer and field summaries
- active crop cycles
- tasks
- advisories
- forms
- reference data
- pending media

## Local command queue

Each command includes:

- local id
- idempotency key
- actor
- target
- issued time
- expected version
- payload
- sync state

## Sync flow

```text
Local Draft → Queued → Uploading → Accepted
                         ↘ Conflict / Rejected
```

## Conflict handling

- deterministic merge for safe append-only records
- user or supervisor review for conflicting edits
- preserve both versions
- audit resolution

## Security

- encrypted local storage where supported
- session expiry
- minimum cached data
- secure logout
- remote revocation on reconnect
