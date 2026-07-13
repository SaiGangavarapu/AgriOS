# BRD 61 — Offline-First Operation and Synchronization

## 1. Objective

Allow essential farmer and field-officer workflows to continue during intermittent or absent connectivity.

## 2. Offline-capable data

- farmer and assigned farm summaries
- field boundaries
- active crop cycles
- recent advisories
- pending tasks
- approved knowledge extracts
- forms
- recent observations
- queued photos
- device summaries
- consent status needed for the workflow

## 3. Offline-capable actions

- view cached tasks
- record operation
- record observation
- capture photos
- complete field visit
- acknowledge advisory
- record irrigation
- capture boundary
- create draft farmer or field registration

Sensitive sharing and irreversible administrative actions may require connectivity.

## 4. Synchronization states

`Local Draft → Queued → Uploading → Server Accepted → Conflict → Rejected → Resolved`

## 5. Conflict rules

Potential conflicts:

- same record edited on two devices
- field boundary changed
- task completed and cancelled
- consent revoked during offline use
- crop stage changed
- duplicate operation recorded

Conflict resolution shall use:

- deterministic merge where safe
- user review
- authorized supervisor review
- preservation of both versions
- audit record

## 6. Data protection

Offline data shall support:

- device encryption where available
- authenticated access
- local session expiry
- remote account revocation on next contact
- minimum cached data
- secure deletion after retention period

## 7. Business requirements

- BR-273: Essential farmer and field-officer workflows shall work offline.
- BR-274: Synchronization shall expose pending, failed, and conflict states.
- BR-275: Duplicate and replayed actions shall be detected.
- BR-276: Conflicts shall preserve original versions and audit history.
- BR-277: Offline storage shall follow minimum-data and security controls.
