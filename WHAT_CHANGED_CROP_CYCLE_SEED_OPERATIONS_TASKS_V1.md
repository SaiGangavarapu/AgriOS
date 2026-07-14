# What Changed — Crop Cycle, Seed, Farm Operations, and Task Calendar Core v1.0

## New bounded-context implementations

1. Crop Cycle
2. Seed
3. Farm Operations
4. Tasks and Calendar

## New migrations

- V011 crop cycle
- V012 seed
- V013 farm operations
- V014 tasks and calendar

The implementation uses explicit lifecycle transitions, tenant-scoped lookups,
optimistic locking, and validation against approved crop plans and crop-cycle area.
