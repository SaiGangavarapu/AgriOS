# BRD 19 — Field-Officer and Assisted Workflows

## 1. Objective

Enable trained personnel to support farmers who have limited connectivity, literacy, time, or confidence using digital tools.

## 2. Field-officer capabilities

- assigned farmer portfolio
- visit calendar
- route planning
- offline farmer search
- assisted onboarding
- field-boundary capture
- soil-sample registration
- crop observation
- issue escalation
- photo and geotag capture
- follow-up tasks
- consent-assisted explanation

## 3. Visit lifecycle

`Planned → Assigned → Started → Completed → Follow-Up Required → Closed`

Exceptional states:

- Cancelled
- Farmer Unavailable
- Access Blocked
- Connectivity Deferred

## 4. Visit record

- officer
- farmer
- farm and field
- purpose
- start and end time
- location
- activities
- observations
- photos
- farmer acknowledgement
- follow-up
- synchronization status

## 5. Assisted consent

The officer may explain but must not accept consent on behalf of the farmer unless legally and procedurally permitted. The platform should record:

- language used
- explanation method
- farmer response
- witness where required
- officer identity

## 6. Business requirements

- BR-061: The platform shall support offline field-officer workflows.
- BR-062: Field visits shall be geotagged and auditable where consented.
- BR-063: Follow-up tasks shall be linked to the visit and field.
- BR-064: Assisted onboarding shall preserve the farmer as the data subject.
- BR-065: Field officers shall not receive unrestricted access to unrelated farmer data.
