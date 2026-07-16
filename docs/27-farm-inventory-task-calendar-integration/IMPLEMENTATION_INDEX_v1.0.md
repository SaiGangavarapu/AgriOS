# Implementation Index v1.0

## Data flow
1. Inventory receipts create a lot and an immutable stock movement.
2. Item quantity is updated transactionally with the movement.
3. Manual adjustments are idempotency-aware.
4. Recurring schedules generate normal task records, preserving the existing task lifecycle and event history.
5. Calendar queries merge task due dates and operation dates without duplicating domain ownership.
6. Dashboard queries aggregate tasks, operations, low stock, expiring lots, and upcoming work.

## APIs
- `POST /api/v1/inventory/items`
- `POST /api/v1/inventory/items/{id}/receipts`
- `POST /api/v1/inventory/items/{id}/adjustments`
- `GET /api/v1/farms/{farmId}/inventory`
- `GET /api/v1/inventory/lots/expiring`
- `POST /api/v1/task-schedules`
- `POST /api/v1/task-schedules/generate-due`
- `GET /api/v1/work-management/calendar`
- `GET /api/v1/work-management/farms/{farmId}/dashboard`

## Architecture
Domain modules retain ownership. `workmanagement` is a read-oriented integration layer and does not mutate task, operation, or inventory records.

## Known limitations
No scheduler invokes due-task generation automatically yet. Operation input consumption is not automatically mapped to inventory items in this release because product/master-data matching rules must be defined first. Finance cost posting and shared-resource booking coordination are also deferred to avoid unsafe implicit accounting or reservations.
