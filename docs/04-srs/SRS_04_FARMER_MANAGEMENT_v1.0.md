# SRS 04 — Farmer Management

## Functional requirements

- FR-FARMER-001: Support self and assisted registration.
- FR-FARMER-002: Support contact verification.
- FR-FARMER-003: Support configurable verification levels.
- FR-FARMER-004: Detect potential duplicates.
- FR-FARMER-005: Merge duplicate profiles through authorized workflow.
- FR-FARMER-006: Support suspension, reactivation, archive, and deceased states.
- FR-FARMER-007: Preserve profile correction history.
- FR-FARMER-008: Support preferred language and communication preferences.
- FR-FARMER-009: Support household and organization links.
- FR-FARMER-010: Keep Farmer separate from UserAccount.

## Key validations

- confirmed duplicate cannot create a second canonical farmer
- merged profile is read-only
- optional identity or financial data shall not block basic advisory access unless required
