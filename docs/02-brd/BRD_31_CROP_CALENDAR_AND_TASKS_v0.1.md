# BRD 31 — Crop Calendar and Tasks

## 1. Objective

Convert the crop plan and current field state into a practical calendar of actions.

## 2. Task sources

- crop knowledge
- approved crop plan
- weather event
- sensor event
- expert instruction
- field observation
- regulatory requirement
- farmer-created task

## 3. Task attributes

- title
- field
- crop cycle
- task type
- planned window
- due date
- priority
- dependency
- instruction
- assignee
- evidence
- reminder
- status
- completion record

## 4. Task lifecycle

`Proposed → Planned → Assigned → In Progress → Completed → Verified`

Exceptional states:

- Overdue
- Deferred
- Skipped
- Cancelled
- Superseded

## 5. Dynamic rescheduling

Tasks may be rescheduled because of:

- weather
- crop-stage change
- incomplete dependency
- field condition
- input unavailability
- labour constraint
- expert revision

## 6. Business requirements

- BR-122: The platform shall generate crop-calendar tasks.
- BR-123: Tasks shall support dependencies and windows.
- BR-124: Weather and stage changes shall re-evaluate tasks.
- BR-125: Overdue tasks shall trigger escalation based on risk.
- BR-126: Completion shall support simple farmer evidence.
