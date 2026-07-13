# BRD 41 — Device Alerts, Maintenance, and Calibration

## 1. Objective

Keep field devices trustworthy, operational, and maintainable throughout their lifecycle.

## 2. Device-health indicators

- online status
- battery
- signal quality
- last message
- sensor drift
- stuck reading
- clock drift
- enclosure issue
- firmware status
- calibration expiry

## 3. Alert types

- device offline
- low battery
- weak signal
- invalid readings
- sensor drift
- calibration due
- firmware issue
- gateway storage nearing capacity
- unauthorized movement
- actuator failure

## 4. Alert lifecycle

`Detected → Open → Assigned → Acknowledged → Resolved → Verified → Closed`

## 5. Maintenance record

- device
- issue
- technician
- visit
- diagnosis
- repair
- replacement part
- firmware change
- calibration
- test result
- cost
- downtime

## 6. Calibration

Calibration shall record:

- method
- reference instrument
- technician
- date
- environment
- before and after result
- correction
- validity
- next due date

## 7. Business requirements

- BR-173: The platform shall monitor device health and calibration expiry.
- BR-174: Device alerts shall support assignment and resolution workflows.
- BR-175: Maintenance and replacement history shall be retained.
- BR-176: Calibration shall be versioned and linked to telemetry.
- BR-177: Uncalibrated devices shall reduce advisory confidence.
