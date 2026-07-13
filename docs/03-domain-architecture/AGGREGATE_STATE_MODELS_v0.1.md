# Aggregate State Models

## Farmer

`Draft → Registered → Contact Verified → Institution Verified → Active`

Exceptional:

- Suspended
- Duplicate Under Review
- Merged
- Archived

Rules:

- Merged is terminal.
- Suspended may return to Active only through authorized review.
- Verification decrease requires reason and audit.

## Farm

`Draft → Registered → Verified → Active → Inactive → Archived`

Rules:

- Archived farm cannot accept new fields.
- Inactive farm may retain active historical records but cannot activate new crop cycles.

## Field

`Draft → Boundary Captured → Validated → Verified → Active`

Exceptional:

- Disputed
- Split
- Merged
- Inactive
- Archived

Rules:

- Split and Merged are lineage states.
- Only Active fields can receive new crop cycles.

## Soil Sample

`Planned → Assigned → Collected → Sealed → Dispatched → Received → Tested → Published`

Exceptional:

- Rejected
- Lost
- Contaminated
- Recollection Required

## Knowledge Item

`Draft → In Review → Approved → Published → Superseded`

Exceptional:

- Rejected
- Withdrawn

Rules:

- Published version is immutable.
- Superseded and Withdrawn versions remain queryable for audit.

## Crop Plan

`Draft → Generated → Farmer Reviewed → Expert Review → Approved → Activated`

Exceptional:

- Rejected
- Cancelled
- Superseded

Rules:

- Activated plan cannot be edited; revision creates a new version.

## Crop Cycle

`Planned → Activated → Land Preparation → Sown → Established → Growing → Reproductive → Harvest Ready → Harvested → Post-Harvest → Closed`

Exceptional:

- Cancelled
- Failed Establishment
- Re-sowing
- Partial Crop Loss
- Total Crop Loss
- Abandoned

## Nutrient Plan

`Draft → Generated → Review Required/Not Required → Approved → Active → Completed`

Exceptional:

- Rejected
- Revised
- Superseded
- Cancelled

## Irrigation Schedule

`Proposed → Approved → Scheduled → Started → Completed → Reconciled`

Exceptional:

- Skipped
- Cancelled
- Failed
- Partially Completed
- Safety Blocked

## Device

`Procured → Registered → Provisioned → Installed → Commissioned → Active`

Exceptional:

- Offline
- Faulty
- Maintenance
- Suspended
- Decommissioned
- Lost
- Replaced

## Advisory

`Draft → System Generated → Review Required/Not Required → Approved → Published → Acknowledged → Completed`

Exceptional:

- Expired
- Superseded
- Withdrawn
- Rejected
- Farmer Declined

## Expert Case

`Created → Triaged → Assigned → In Review → Information Requested → Decision Issued → Communicated → Closed`

## Crop Health Case

`Observed → Suspected → Differential Review → Testing Required/Not Required → Confirmed → Treatment Planned → Treatment Applied → Effectiveness Reviewed → Closed`

## Produce Lot

`Created → Stored/Available → Reserved → Dispatched → Delivered → Closed`

Transformations:

- Split
- Merge
- Regrade
- Repack
- Processed
- Lost

## Consent Grant

`Draft → Presented → Granted → Active → Expired`

Exceptional:

- Revoked
- Rejected
- Superseded
