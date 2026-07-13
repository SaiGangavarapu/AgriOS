# DB 20 — Indexing and Performance

## Index categories

- primary key
- unique business key
- tenant and status
- foreign-reference id
- date-range
- spatial
- full-text
- partial index for active rows
- composite index for common query patterns

## Examples

- active farmer by tenant and mobile
- active fields by farm
- current soil profile by field
- active crop cycle by field
- upcoming tasks by assignee and due date
- current advisories by field and validity
- active device by assignment
- telemetry by device and time
- produce-lot balance by lot
- consent by subject, recipient, purpose, and validity

## Rule

Indexes are justified by query patterns and verified through execution plans.
