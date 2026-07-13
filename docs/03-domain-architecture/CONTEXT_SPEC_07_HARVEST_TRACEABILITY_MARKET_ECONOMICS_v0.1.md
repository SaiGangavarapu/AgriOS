# Context Specification 07 — Harvest, Traceability, Market, and Economics

## Harvest and Post-Harvest Context

### Aggregate roots

- HarvestPlan
- HarvestEvent
- StorageUnit

### Commands

- CreateHarvestPlan
- RecordHarvest
- RecordPartialHarvest
- RecordPostHarvestOperation
- GradeProduce
- StoreProduce
- RecordStorageLoss

### Invariants

- harvest event requires crop cycle and area
- quantity and quality loss are distinct
- storage capacity cannot be exceeded without exception

## Produce Traceability Context

### Aggregate root

ProduceLot

### Commands

- CreateProduceLot
- SplitProduceLot
- MergeProduceLots
- TransformProduceLot
- TransferLotOwnership
- CorrectLotBalance

### Invariants

- quantity balance cannot be negative
- child lots must trace to source lots
- unsupported certification claim is prohibited

## Market Context

### Aggregate roots

- Buyer
- BuyerRequirement
- BuyerOffer
- DeliveryArrangement

### Commands

- RegisterBuyer
- PublishBuyerRequirement
- RecordBuyerOffer
- AcceptBuyerOffer
- ScheduleDelivery
- ConfirmDelivery
- RecordPaymentStatus

### Invariants

- offer must identify commodity, grade, unit, price, validity, and terms
- accepted offer changes require controlled amendment
- agronomic ranking cannot depend on hidden commercial sponsorship

## Farm Economics Context

### Aggregate root

CropCycleEconomics

### Commands

- RecordCost
- RecordRevenue
- AllocateSharedCost
- RecalculateMargin
- RunEconomicScenario
- CloseEconomicPeriod

### Invariants

- missing, estimated, allocated, and confirmed values remain distinct
- negative quantity or double-counted revenue is prohibited
- unaudited values cannot be labelled audited
