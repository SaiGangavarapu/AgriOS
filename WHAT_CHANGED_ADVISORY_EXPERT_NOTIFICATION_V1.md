# What Changed — Advisory, Expert Review, and Farmer Notification Core v1.0

Added the Advisory and Notification bounded contexts.

Advisories are evidence-backed records with explicit lifecycle states.
Expert review is auditable and supports assignment, visible/internal notes,
decisions, follow-up, and optional advisory linkage.

Notification delivery is provider-neutral. The core stores channel, payload,
idempotency, attempts, provider result, failure state, delivery, and read status.
Actual SMS, WhatsApp, email, and voice providers remain adapters.
