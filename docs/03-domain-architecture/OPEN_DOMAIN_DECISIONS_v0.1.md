# Open Domain Decisions

## D-001: Farmer versus user

Decide whether every farmer must have a user account or whether Farmer remains a separate domain identity with optional account linkage.

## D-002: Farm and field ownership

Decide whether WaterSource remains in Farm and Field or becomes an independent bounded context in later releases.

## D-003: Crop-health boundary

Decide whether Scouting remains inside Pest and Disease or belongs to Farm Operations with a published observation contract.

## D-004: Task ownership

Decide whether Task and Calendar is a generic orchestration context or whether each business context owns its own tasks and publishes reminders.

## D-005: Advisory ownership

Decide whether Advisory owns recommendation lifecycle only or also owns rule execution.

## D-006: Knowledge granularity

Decide whether Agronomy Knowledge remains one context or later splits into Crop Knowledge, Input Knowledge, and Crop-Health Knowledge.

## D-007: Telemetry partitioning

Decide whether Telemetry is one bounded context or infrastructure owned by IoT Device with analytical projections elsewhere.

## D-008: Market transaction boundary

Decide whether AgriOS supports buyer coordination only or full order, invoice, and payment workflows.

## D-009: Economics boundary

Decide whether Farm Economics remains analytical or becomes accounting-grade in future ERP integration.

## D-010: Existing-product integration

Decide identity, consent, and data-sharing boundaries with Milk Register, Livestock, Farm Fresh, and Seller ERP.

## D-011: Tenant model

Decide whether tenant represents deployment customer, institution, FPO, programme, or a combination.

## D-012: Geography authority

Decide the authoritative source and versioning model for administrative geography and cadastral references.

## D-013: Expert identity

Decide whether expert profiles belong in Identity, Farmer/Organization, or a dedicated Expert Services context.

## D-014: Event platform

Decide when in-process Spring Modulith events should be supplemented by Kafka or Redpanda.

## D-015: Shared-kernel scope

Approve the exact shared value-object list before coding begins.
