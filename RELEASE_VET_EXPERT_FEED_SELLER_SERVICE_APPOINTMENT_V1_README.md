# AgriOS Vet, Expert, Feed Seller, and Service Appointment Management v1.0

This cumulative milestone introduces a provider-neutral agricultural service directory and appointment workflow.

## Scope

- Vet, agronomist, crop, soil, irrigation, feed seller, input seller, and equipment service provider profiles.
- Optional link from service providers to existing advisory expert profiles.
- Provider availability and capacity.
- Farmer appointment request, confirmation, execution, completion, rejection, cancellation, and no-show lifecycle.
- Farm, field, crop-cycle, and generic subject references.
- Post-visit diagnosis, recommendations, prescriptions, attachments, and follow-up dates.
- Appointment-backed provider reviews and aggregate ratings.
- Farmer appointment history and provider dashboard.

## Architectural boundaries

Expert identity is reused through `advisory.expert_profile`. Feed seller product catalogue, stock, pricing, orders, and payments are not duplicated in this module. Animal-specific clinical records are deferred until the Livestock Integration Gateway milestone.

## Database

Adds `V047__service_provider_appointment_management.sql` and the `service` schema.

## Known limitations

- No payment collection or commission handling.
- No automatic notification dispatch yet.
- No provider calendar synchronization.
- No animal clinical record link until livestock integration.
- No teleconsultation/video implementation.
- No advanced review moderation or recommendation ranking.
