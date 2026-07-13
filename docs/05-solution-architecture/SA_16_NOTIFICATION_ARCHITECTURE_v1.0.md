# SA 16 — Notification Architecture

## Channels

- in-app
- push
- SMS
- voice/IVR
- WhatsApp or approved messaging
- email
- field-officer task

## Components

- notification template service
- preference service
- channel router
- delivery provider adapters
- fallback engine
- delivery tracking
- acknowledgement

## Priority

- Critical
- High
- Normal
- Low

Critical alerts support fallback and expiry. Non-critical messages respect quiet hours and preferences.
