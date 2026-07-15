# What Changed — Farmer Finance, Cost Accounting, Profitability, and Income Aggregation Core v1.0

Added the Finance bounded context and a provider-neutral financial-event ingestion model.

Every AgriOS module can emit an immutable event using a stable idempotency key.
The finance context stores the source, amount, direction, farmer, field,
crop cycle, cost center, and original payload.

Profitability is calculated from posted inflow and outflow events and can be
reported at farmer, field, or crop-cycle level.

The database also contains the double-entry accounting foundation so automatic
journal posting can be expanded without changing the upstream event contract.
