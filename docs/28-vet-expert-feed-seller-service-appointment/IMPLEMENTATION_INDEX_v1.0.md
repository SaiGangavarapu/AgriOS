# Implementation Index v1.0

## Module

`com.agrios.platform.serviceappointment`

## Data flow

1. Tenant registers a service provider, optionally linking an existing expert profile.
2. Provider publishes availability slots.
3. Farmer selects provider and slot and submits problem context.
4. Provider confirms, starts, and completes the appointment.
5. Provider records visit recommendations and follow-up information.
6. Farmer submits one review for the completed appointment.
7. Provider aggregate rating is recalculated from published reviews.

## APIs

- `POST /api/v1/service-providers`
- `GET /api/v1/service-providers?type=VET`
- `POST /api/v1/service-providers/{providerId}/availability`
- `GET /api/v1/service-providers/{providerId}/availability`
- `POST /api/v1/service-appointments`
- `POST /api/v1/service-appointments/{id}/status`
- `POST /api/v1/service-appointments/{id}/visit-note`
- `POST /api/v1/service-appointments/{id}/review`
- `GET /api/v1/farmers/{farmerId}/service-appointments`
- `GET /api/v1/service-providers/{providerId}/dashboard`

## Next milestones

1. Animal and Livestock Integration Gateway v1.0.
2. Farm-Fresh Produce, Milk, Meat, and Egg Integration v1.0.
3. Farmer Unified Income and Expense Ledger v1.0.
