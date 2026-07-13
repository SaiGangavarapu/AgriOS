# ADR-0005: Domain-Owned Tasks with a Central Task Projection

## Status

Accepted

## Context

Crop planning, nutrient, irrigation, crop health, device maintenance, and support all generate actionable work. A single generic task engine would weaken domain rules, while separate task lists would fragment the user experience.

## Decision

Each domain owns the lifecycle and meaning of its work item. The Task and Calendar context maintains a unified projection, assignment, reminder, and completion-routing layer.

## Consequences

- domain rules remain with the source context
- farmer sees one consolidated task list
- completion is routed back to the owning context
- task projection is eventually consistent and rebuildable
