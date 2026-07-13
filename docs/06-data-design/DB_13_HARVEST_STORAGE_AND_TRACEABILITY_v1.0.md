# DB 13 — Harvest, Storage, and Traceability

## Harvest

- `harvest.harvest_plan`
- `harvest.harvest_event`
- `harvest.harvest_quality`
- `harvest.post_harvest_operation`
- `harvest.storage_unit`
- `harvest.storage_event`
- `harvest.storage_loss`

## Traceability

- `traceability.produce_lot`
- `traceability.lot_parent`
- `traceability.lot_transformation`
- `traceability.lot_quality`
- `traceability.lot_location`
- `traceability.lot_ownership`
- `traceability.lot_quantity_ledger`

## Integrity

- quantity ledger append-only
- lot balance cannot become negative
- split and merge preserve ancestry
- unsupported certification claim blocked
