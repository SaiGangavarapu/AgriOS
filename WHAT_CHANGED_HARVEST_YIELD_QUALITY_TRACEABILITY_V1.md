# What Changed — Harvest, Yield, Quality, and Traceability Core v1.0

Added three bounded contexts:

1. Harvest
2. Yield and Quality
3. Traceability and Storage

The implementation provides an executable farm-to-lot chain:

crop cycle -> harvest plan -> harvest batch -> yield record ->
quality assessment -> traceability lot -> package -> QR trace lookup.

The QR response exposes a governed trace snapshot, not unrestricted internal data.
