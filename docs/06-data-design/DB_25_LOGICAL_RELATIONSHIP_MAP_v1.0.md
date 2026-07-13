# DB 25 — Logical Relationship Map

```text
Tenant
 ├── Programme
 ├── UserAccount
 ├── Farmer
 │    ├── HouseholdMembership
 │    ├── OrganizationMembership
 │    ├── FarmParticipant
 │    └── ConsentGrant
 ├── Farm
 │    ├── Field
 │    │    ├── BoundaryVersion
 │    │    ├── ManagementZone
 │    │    ├── SoilProfile
 │    │    ├── CropPlan
 │    │    ├── CropCycle
 │    │    ├── DeviceAssignment
 │    │    └── Advisory
 │    └── WaterSource
 ├── CropCycle
 │    ├── SeedAllocation
 │    ├── Operation
 │    ├── NutrientPlan
 │    ├── IrrigationPlan
 │    ├── CropHealthCase
 │    ├── HarvestEvent
 │    └── Economics
 └── ProduceLot
      ├── LotTransformation
      ├── StorageEvent
      ├── BuyerOffer
      └── Delivery
```

This map is logical. Cross-context physical foreign keys are not implied.
